package rstoolkit.injection;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.ProtectionDomain;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import rstoolkit.Boot;
import rstoolkit.api.osrs.DirectGraphicsBuffer;
import rstoolkit.callback.Callbacks;
import rstoolkit.injection.Rs2Hooks.FieldHook;

public class Injector implements ClassFileTransformer, Opcodes {

	private File workingDir;
	private boolean loaded;
	
	public Injector(File workingDir) {
		this.workingDir = workingDir;
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		try {
			if (!loaded && "Rs2Applet".equals(className)) {
				loader.loadClass("netscape.javascript.JSObject");
				loaded = true;
			}
			if (isAccessed(className)) {
				ClassNode cn = new ClassNode();
				ClassReader cr = new ClassReader(classfileBuffer);
				cr.accept(cn, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
				transformRSClass(cn, Class.forName(Hooks.getAccessor(className)));
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				cn.accept(cw);
				byte[] result = cw.toByteArray();
				File f = new File(workingDir, "/debug/" + className + ".class");
				f.getParentFile().mkdirs();
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(result);
				fos.close();
//				if (className.equals("gv"))
//					System.exit(0);
				return result;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private void transformRSClass(ClassNode cn, Class<?> accessor) {
		cn.interfaces.add(accessor.getName().replace('.', '/'));
		implementInterface(cn, accessor);
		// TODO: implement 
		if ("client".equals(cn.name)) {
			for (MethodNode mn : cn.methods) {
				if (mn.name.equals("init")) {
					System.out.println("Added callback to " + cn.name + '.' + mn.name + mn.desc);
					mn.instructions.insert(new MethodInsnNode(INVOKESTATIC, Callbacks.class.getName().replace('.', '/'),
							"onLoad", "(L" + Boot.getMainClass().getName().replace('.', '/') + ";)V", false));
					mn.instructions.insert(new VarInsnNode(ALOAD, 0));
					System.out.println("Injected onLoad().");
				}
			}
		} else if (cn.name.equals(Hooks.getMapping().get(DirectGraphicsBuffer.class.getName()))) {
			System.out.println(Hooks.getMapping().get(DirectGraphicsBuffer.class.getName()));
			for (MethodNode mn : cn.methods) {
				for (Iterator<AbstractInsnNode> it = mn.instructions.iterator(); it.hasNext();) {
					AbstractInsnNode insn = it.next();
					if (insn.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) insn).name.equals("drawImage")) {
						mn.instructions.insert(new MethodInsnNode(INVOKESTATIC,
								Callbacks.class.getName().replace('.', '/'), "onPaint", "()V", false));
						mn.instructions.insert(new VarInsnNode(ALOAD, 0));
						System.out.println("Injected onPaint().");
					}
				}
			}
		} else if (cn.name.equals("ck")) {
			System.out.println("Callback....");
			for (MethodNode mn : cn.methods) {
				if (mn.name.equals("ax") && mn.desc.equals("(I)V")) {
					System.out.println("We're in");
					InsnList insns = new InsnList();
					insns.add(new VarInsnNode(ILOAD, 3));
					insns.add(new MethodInsnNode(INVOKESTATIC,
							Callbacks.class.getName().replace('.', '/'), "onPacket", "(I)V", false));
					mn.instructions.insert(mn.instructions.get(84), insns);
				}
			}
		}
	}

	private void implementInterface(ClassNode cn, Class<?> accClass) {
		for (Method m : accClass.getDeclaredMethods()) {
			Getter g = m.getAnnotation(Getter.class);
			Setter s = m.getAnnotation(Setter.class);
			if (g != null) {
				injectGetter(m, g.value(), cn, accClass);
			} else if (s != null) {
				injectSetter(m, s.value(), cn, accClass);
			}
		}
	}

	private void injectGetter(Method m, String fieldName, ClassNode cn, Class<?> accClass) {
		FieldHook fh = Hooks.getFieldAccessor(accClass.getName(), fieldName);
		if (fh == null) {
			System.out.println("Broken hook: " + accClass.getName() + '.' + fieldName);
			return;
		}
		MethodVisitor mv = cn.visitMethod(ACC_PUBLIC, m.getName(), Type.getMethodDescriptor(m), null, null);
		mv.visitCode();
		Type rt = Type.getReturnType(m);
		if (fh.hasObfuscatedOwner()) {
			mv.visitFieldInsn(GETSTATIC, fh.getObfuscatedOwner(), fh.getObfuscatedName(), mapDesc(rt));
		} else {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, cn.name, fh.getObfuscatedName(), mapDesc(rt));
		}
		if (fh.hasMultiplier()) {
			mv.visitLdcInsn(fh.getMultiplier());
			mv.visitInsn(IMUL);
		}
		mv.visitInsn(rt.getOpcode(IRETURN));
	}

	private void injectSetter(Method m, String fieldName, ClassNode cn, Class<?> accClass) {
		MethodVisitor mv = cn.visitMethod(ACC_PUBLIC, m.getName(), Type.getMethodDescriptor(m), null, null);
		mv.visitCode();
		FieldHook fh = Hooks.getFieldAccessor(accClass.getName(), fieldName);
		Type at = Type.getArgumentTypes(m)[0];
		if (!fh.hasObfuscatedOwner())
			mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(at.getOpcode(ILOAD), 1);
		if (fh.hasMultiplier()) {
			mv.visitLdcInsn(BigInteger.valueOf(fh.getMultiplier()).modInverse(BigInteger.ONE.shiftLeft(32)).intValue());
			mv.visitInsn(IMUL);
		}
		if (fh.hasObfuscatedOwner()) 
			mv.visitFieldInsn(PUTSTATIC, fh.getObfuscatedOwner(), fh.getObfuscatedName(), mapDesc(at));
		else
			mv.visitFieldInsn(PUTFIELD, cn.name, fh.getObfuscatedName(), mapDesc(at));
		mv.visitInsn(RETURN);
	}

	private static String mapDesc(Type t) {
		switch (t.getSort()) {
		case Type.ARRAY:
			String s = mapDesc(t.getElementType());
			for (int i = 0; i < t.getDimensions(); ++i) {
				s = '[' + s;
			}
			return s;
		case Type.OBJECT:
			String newType = map(t.getInternalName().replace('/', '.'));
			if (newType != null) {
				return 'L' + newType + ';';
			}
		}
		return t.getDescriptor();
	}

	private static String map(String type) {
		return Hooks.getMapping().get(type);
	}

	private boolean isAccessed(String className) {
		if (Hooks.getAccessor(className) != null) {
			try {
				return Class.forName(Hooks.getAccessor(className)) != null;
			} catch (ClassNotFoundException e) {
			}
//			return new File(binaryRoot, Hooks.getAccessor(className).replace('.', '/').concat(".class")).exists();
		}
		return false;
	}

}
