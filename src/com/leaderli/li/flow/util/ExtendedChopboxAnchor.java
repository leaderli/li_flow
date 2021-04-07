package com.leaderli.li.flow.util;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import com.leaderli.li.flow.constant.PluginConstant;

public class ExtendedChopboxAnchor extends ChopboxAnchor {
    private static final Dimension EMPTY_DIMENSION = new Dimension(0, 0);
    private int position = 0;

    public ExtendedChopboxAnchor(IFigure owner, int position) {
        super(owner);
        this.position = position;
    }

    protected Rectangle getBox() {
        Rectangle box = super.getBox();
        switch (getPosition()) {
            case PluginConstant.CHOPBOX_ANCHOR_TOP:
                return new Rectangle(box.getTop(), EMPTY_DIMENSION);
            case PluginConstant.CHOPBOX_ANCHOR_BOTTOM:
                return new Rectangle(box.getBottom(), EMPTY_DIMENSION);
            case PluginConstant.CHOPBOX_ANCHOR_LEFT:
                return new Rectangle(box.getLeft(), EMPTY_DIMENSION);
            case PluginConstant.CHOPBOX_ANCHOR_TOP_LEFT:
                return new Rectangle(box.getTopLeft(), EMPTY_DIMENSION);
            case PluginConstant.CHOPBOX_ANCHOR_BOTTOM_LEFT:
                return new Rectangle(box.getBottomLeft(), EMPTY_DIMENSION);
            case PluginConstant.CHOPBOX_ANCHOR_RIGHT:
                return new Rectangle(box.getRight(), EMPTY_DIMENSION);
            case PluginConstant.CHOPBOX_ANCHOR_TOP_RIGHT:
                return new Rectangle(box.getTopRight(), EMPTY_DIMENSION);
            case PluginConstant.CHOPBOX_ANCHOR_BOTTOM_RIGHT:
                return new Rectangle(box.getBottomRight(), EMPTY_DIMENSION);
            default:
                return box;
        }
    }

    public int getPosition() {
        return this.position;
    }
}
