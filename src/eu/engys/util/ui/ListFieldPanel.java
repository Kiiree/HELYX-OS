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

package eu.engys.util.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import eu.engys.util.ui.textfields.StringField;

public class ListFieldPanel extends JPanel {
    private StringField textField;
    private JButton button;

    public ListFieldPanel(ListBuilder listBuilder) {
        super(new BorderLayout(4, 4));
        setName("list.field.panel");

        textField = new StringField();
        textField.setName("field");
        textField.setColumns(20);
        textField.setEnabled(false);

        button = createButtonDoubleList(listBuilder);
        button.setName("button");
        add(textField, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        // textField.setEnabled(enabled);
        button.setEnabled(enabled);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (textField != null)
            textField.addPropertyChangeListener(listener);
    }

    private JButton createButtonDoubleList(ListBuilder listBuilder) {
        JButton button = new JButton(new DoubleListAction(this, listBuilder));
        Dimension prefSize = button.getPreferredSize();
        button.setPreferredSize(new Dimension(24, prefSize.height));
        return button;
    }

    public String[] getValues() {
        String text = textField.getText().trim();
        if (text.length() == 0)
            return new String[0];
        else
            return text.split("\\s+");
    }

    public void setValues(String[] values) {
        StringBuilder sb = new StringBuilder();
        for (String string : values) {
            sb.append(string + " ");
        }
        textField.setValue(sb.toString());
    }
    
    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
        button.setToolTipText(text);
        textField.setToolTipText(text);
    }

}
