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

package eu.engys.core.controller;

import java.io.File;
import java.util.List;

import eu.engys.core.project.Model;
import eu.engys.util.IOUtils;
import eu.engys.util.Util;

public abstract class AbstractScriptFactory implements ScriptFactory {

    protected static final String RUN_MESH = "Run Mesh";
    public static final String MESH_SERIAL_RUN = "mesh_serial.run";
    public static final String MESH_SERIAL_BAT = "mesh_serial.bat";
    public static final String MESH_PARALLEL_RUN = "mesh_parallel.run";
    public static final String MESH_PARALLEL_BAT = "mesh_parallel.bat";

    protected static final String CHECK_MESH = "Check Mesh";
    public static final String CHECK_MESH_SERIAL_RUN = "check_mesh_serial.run";
    public static final String CHECK_MESH_SERIAL_BAT = "check_mesh_serial.bat";
    public static final String CHECK_MESH_PARALLEL_RUN = "check_mesh_parallel.run";
    public static final String CHECK_MESH_PARALLEL_BAT = "check_mesh_parallel.bat";

    protected static final String RUN_CASE = "Run Case";
    public static final String SOLVER_SERIAL_RUN = "solver_serial.run";
    public static final String SOLVER_SERIAL_BAT = "solver_serial.bat";
    public static final String SOLVER_PARALLEL_RUN = "solver_parallel.run";
    public static final String SOLVER_PARALLEL_BAT = "solver_parallel.bat";

    protected static final String INITIALISE_FIELDS = "Initialise Fields";
    public static final String INITIALISE_FIELDS_SERIAL_RUN = "initialiseFields_serial.run";
    public static final String INITIALISE_FIELDS_SERIAL_BAT = "initialiseFields_serial.bat";
    public static final String INITIALISE_FIELDS_PARALLEL_RUN = "initialiseFields_parallel.run";
    public static final String INITIALISE_FIELDS_PARALLEL_BAT = "initialiseFields_parallel.bat";

    protected static final String EXTRUDEMESH = "Extrude To Region";
    private static final String EXTRUDEMESH_SERIAL_RUN = "extrudeMesh_serial.run";
    private static final String EXTRUDEMESH_SERIAL_BAT = "extrudeMesh_serial.bat";
    private static final String EXTRUDEMESH_PARALLEL_RUN = "extrudeMesh_parallel.run";
    private static final String EXTRUDEMESH_PARALLEL_BAT = "extrudeMesh_parallel.bat";

    /*
     * MESH
     */
    @Override
    public File getMeshScript(Model model) {
        File parallelScript = getMeshParallelScript(model);
        File serialScript = getMeshSerialScript(model);

        if (model.getProject().isParallel()) {
            return parallelScript;
        } else {
            return serialScript;
        }
    }

    @Override
    public void deleteMeshScripts(Model model) {
        File serialFile = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? MESH_SERIAL_BAT : MESH_SERIAL_RUN);
        if (serialFile.exists()) {
            serialFile.delete();
        }
        File parallelFile = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? MESH_PARALLEL_BAT : MESH_PARALLEL_RUN);
        if (parallelFile.exists()) {
            parallelFile.delete();
        }
    }

    @Override
    public List<String> getDefaultMeshScript(Model model) {
        List<String> script = null;
        if (model.getProject().isParallel()) {
            script = getParallelMeshScript();
        } else {
            script = getSerialMeshScript();
        }
        return script;
    }

    private File getMeshParallelScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? MESH_PARALLEL_BAT : MESH_PARALLEL_RUN);
        writeFileIfNeeded(file, getParallelMeshScript());
        file.setExecutable(true);
        return file;
    }

    private File getMeshSerialScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? MESH_SERIAL_BAT : MESH_SERIAL_RUN);
        writeFileIfNeeded(file, getSerialMeshScript());
        file.setExecutable(true);
        return file;
    }

    protected abstract List<String> getParallelMeshScript();

    protected abstract List<String> getSerialMeshScript();

    @Override
    public File getCheckMeshScript(Model model) {
        File parallelScript = getCheckMeshParallelScript(model);
        File serialScript = getCheckMeshSerialScript(model);

        if (model.getProject().isParallel()) {
            return parallelScript;
        } else {
            return serialScript;
        }
    }

    @Override
    public List<String> getDefaultCheckMeshScript(Model model) {
        List<String> script = null;
        if (model.getProject().isParallel()) {
            script = getParallelMeshScript();
        } else {
            script = getSerialMeshScript();
        }
        return script;
    }

    private File getCheckMeshParallelScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? CHECK_MESH_PARALLEL_BAT : CHECK_MESH_PARALLEL_RUN);
        writeFileIfNeeded(file, getParallelCheckMeshScript());
        file.setExecutable(true);
        return file;
    }

    private File getCheckMeshSerialScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? CHECK_MESH_SERIAL_BAT : CHECK_MESH_SERIAL_RUN);
        writeFileIfNeeded(file, getSerialCheckMeshScript());
        file.setExecutable(true);
        return file;
    }

    protected abstract List<String> getParallelCheckMeshScript();

    protected abstract List<String> getSerialCheckMeshScript();

    /*
     * SOLVER
     */

    @Override
    public File getSolverScript(Model model) {
        File parallelScript = getSolverParallelScript(model);
        File serialScript = getSolverSerialScript(model);

        if (model.getProject().isParallel()) {
            return parallelScript;
        } else {
            return serialScript;
        }
    }

    @Override
    public List<String> getDefaultSolverScript(Model model) {
        List<String> script = null;
        if (model.getProject().isParallel()) {
            script = getParallelSolverScript();
        } else {
            script = getSerialSolverScript();
        }
        return script;
    }

    protected File getSolverParallelScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? SOLVER_PARALLEL_BAT : SOLVER_PARALLEL_RUN);
        writeFileIfNeeded(file, getParallelSolverScript());
        file.setExecutable(true);
        return file;
    }

    protected File getSolverSerialScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? SOLVER_SERIAL_BAT : SOLVER_SERIAL_RUN);
        writeFileIfNeeded(file, getSerialSolverScript());
        file.setExecutable(true);
        return file;
    }

    protected abstract List<String> getParallelSolverScript();

    protected abstract List<String> getSerialSolverScript();

    @Override
    public File getInitialiseScript(Model model) {
        File parallelScript = getInitialiseParallelScript(model);
        File serialScript = getInitialiseSerialScript(model);

        if (model.getProject().isParallel()) {
            return parallelScript;
        } else {
            return serialScript;
        }
    }

    protected File getInitialiseParallelScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? INITIALISE_FIELDS_PARALLEL_BAT : INITIALISE_FIELDS_PARALLEL_RUN);
        writeFileIfNeeded(file, getParallelInitialiseScript());
        file.setExecutable(true);
        return file;
    }

    protected File getInitialiseSerialScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? INITIALISE_FIELDS_SERIAL_BAT : INITIALISE_FIELDS_SERIAL_RUN);
        writeFileIfNeeded(file, getSerialInitialiseScript());
        file.setExecutable(true);
        return file;
    }

    protected abstract List<String> getParallelInitialiseScript();

    protected abstract List<String> getSerialInitialiseScript();

    @Override
    public List<String> getDefaultInitialiseScript(Model model) {
        List<String> script = null;
        if (model.getProject().isParallel()) {
            script = getParallelInitialiseScript();
        } else {
            script = getSerialInitialiseScript();
        }
        return script;
    }

    @Override
    public File getExtrudeScript(Model model) {
        File parallelScript = getExtrudeParallelScript(model);
        File serialScript = getExtrudeSerialScript(model);

        if (model.getProject().isParallel()) {
            return parallelScript;
        } else {
            return serialScript;
        }
    }

    protected File getExtrudeParallelScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? EXTRUDEMESH_PARALLEL_BAT : EXTRUDEMESH_PARALLEL_RUN);
        writeFileIfNeeded(file, getParallelExtrudeScript());
        file.setExecutable(true);
        return file;
    }

    protected File getExtrudeSerialScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), Util.isWindowsScriptStyle() ? EXTRUDEMESH_SERIAL_BAT : EXTRUDEMESH_SERIAL_RUN);
        writeFileIfNeeded(file, getSerialExtrudeScript());
        file.setExecutable(true);
        return file;
    }

    protected abstract List<String> getParallelExtrudeScript();

    protected abstract List<String> getSerialExtrudeScript();

    @Override
    public List<String> getDefaultExtrudeScript(Model model) {
        List<String> script = null;
        if (model.getProject().isParallel()) {
            script = getParallelExtrudeScript();
        } else {
            script = getSerialExtrudeScript();
        }
        return script;
    }

    @Override
    public File getQueueLauncher(Model model) {
        // if (Util.isWindowsScriptStyle()) {
        // File file = new File(model.getProject().getBaseDir(), "driver.pbs");
        // writeFileIfNeeded(file, getWindowsQueueLauncher());
        // return file;
        // } else {
        File file = new File(model.getProject().getBaseDir(), "pbs.run");
        writeFileIfNeeded(file, getLinuxQueueLauncher());
        return file;
        // }
    }

    protected abstract List<String> getWindowsQueueLauncher();

    protected abstract List<String> getLinuxQueueLauncher();

    @Override
    public List<String> getDefaultQueueLauncher(Model model) {
        return getLinuxQueueLauncher();
    }

    protected abstract List<String> getWindowsSetupCaseScript();

    protected abstract List<String> getLinuxSetupCaseScript();

    @Override
    public File getExportScript(Model model) {
        File file = new File(model.getProject().getBaseDir(), "export.py");
        writeFileIfNeeded(file, getExportScript());
        return file;
    }

    @Override
    public List<String> getDefaultExportScript(Model model) {
        return getExportScript();
    }

    protected abstract List<String> getExportScript();

    protected void writeFileIfNeeded(File file, List<String> script) {
        if (!file.exists()) {
            IOUtils.writeLinesToFile(file, script);
            file.setExecutable(true);
        }
    }
}
