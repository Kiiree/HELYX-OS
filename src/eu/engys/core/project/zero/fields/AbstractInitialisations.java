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


package eu.engys.core.project.zero.fields;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.engys.core.dictionary.Dictionary;
import eu.engys.core.project.Model;
import eu.engys.util.ArchiveUtils;
import eu.engys.util.progress.ProgressMonitor;

public abstract class AbstractInitialisations implements Initialisations {

    private static Logger logger = LoggerFactory.getLogger(AbstractInitialisations.class);

    protected Map<String, Dictionary> map = new HashMap<>();

    protected final Model model;

    public AbstractInitialisations(Model model) {
        this.model = model;
    }

    public Dictionary getInitializationFor(String field) {
        return map.get(field);
    }

    protected void readFieldFromFile(Field field, File zeroDir, ProgressMonitor monitor) {
        File file = new File(zeroDir, field.getName());
        if (file.exists()) {
            field.read(file);
            logger.info("READ: Field {}", field.getName());
        } else {
            File fileGZ = new File(zeroDir, field.getName() + "." + ArchiveUtils.GZ);
            if (fileGZ.exists()) {
                ArchiveUtils.unGZ(fileGZ, zeroDir);
                field.read(file);
                logger.info("READ: Field {}", field.getName());
            } else {
                monitor.warning("Missing field file " + field.getName(), 2);
                logger.warn("READ: Missing field {}", field.getName());
            }
        }
    }

    // For test purpouse only!
    public Map<String, Dictionary> getMap() {
        return map;
    }

}
