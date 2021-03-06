/*--------------------------------*- Java -*---------------------------------*\
 |		 o                                                                   |                                                                                     
 |    o     o       | HelyxOS: The Open Source GUI for OpenFOAM              |
 |   o   O   o      | Copyright (C) 2012-2016 ENGYS                          |
 |    o     o       | http://www.engys.com                                   |
 |       o          |                                                        |
 |---------------------------------------------------------------------------|
 |	 License                                                                 |
 |   This file is part of HelyxOS.                                           |
 |                                                                           |
 |   HelyxOS is free software; you can redistribute it and/or modify it      |
 |   under the terms of the GNU General Public License as published by the   |
 |   Free Software Foundation; either version 2 of the License, or (at your  |
 |   option) any later version.                                              |
 |                                                                           |
 |   HelyxOS is distributed in the hope that it will be useful, but WITHOUT  |
 |   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or   |
 |   FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License   |
 |   for more details.                                                       |
 |                                                                           |
 |   You should have received a copy of the GNU General Public License       |
 |   along with HelyxOS; if not, write to the Free Software Foundation,      |
 |   Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA            |
\*---------------------------------------------------------------------------*/


package eu.engys.util.ui.builder;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;


public class JCheckBoxController extends JCheckBox implements GroupController {

    private String selectedKey;

    public JCheckBoxController(String name) {
        super(name);
        setName(name);
        setOpaque(false);
        setFocusable(false);
    }

    @Override
    public void addActionListener(ActionListener action) {
        super.addActionListener(action);
    }

    @Override
    public void addGroup(String groupKey, String groupName) {
        this.selectedKey = groupKey;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void setSelectedIndex(int i) {
        super.doClick();
    }

    @Override
    public void setSelectedItem(String groupName) {
        super.doClick();
    }

    @Override
    public void setSelectedKey(String key) {
        super.doClick();
    }

    @Override
    public String getSelectedKey() {
        return isSelected() ? selectedKey : null;
    }

    @Override
    public void addChildController(GroupController controller) {
    }

    @Override
    public GroupController getChildController(String selectedKey) {
        return null;
    }

    @Override
    public Font getFont() {
        Font font = super.getFont();
        return font != null ? font.deriveFont(Font.BOLD) : font;
    }

    @Override
    public Color getForeground() {
        // if (isEnabled()) return Color.BLUE;
        return super.getForeground();
    }
}
