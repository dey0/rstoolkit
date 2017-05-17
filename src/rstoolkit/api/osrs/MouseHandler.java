package rstoolkit.api.osrs;

import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import rstoolkit.injection.Accessor;

@Accessor("MouseHandler")
public interface MouseHandler extends MouseListener, MouseMotionListener, FocusListener {

}
