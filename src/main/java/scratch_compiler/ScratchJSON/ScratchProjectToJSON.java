package scratch_compiler.ScratchJSON;

import scratch_compiler.ScratchProject;
import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.ScratchObjects.Figure;

public class ScratchProjectToJSON {
    public static ObjectJSON projectToJSON(ScratchProject project) {
        ObjectJSON projectJSON = new ObjectJSON();
        projectJSON.setValue("targets", getTargets(project));
        projectJSON.setValue("monitors", new ArrayJSON());
        projectJSON.setValue("extensions", new ArrayJSON());
        projectJSON.setValue("meta", getMetadata());

        return projectJSON;
    }

    private static ArrayJSON getTargets(ScratchProject project) {
        ArrayJSON targets = new ArrayJSON();
        targets.addObject(ScratchObjectToJSON.backgroundToJSON(project.getBackground(), project.getGlobalVariables()));
        for (Figure figure : project.getFigures())
            targets.addObject(ScratchObjectToJSON.figureToJSON(figure));
        return targets;
    }

    private static ObjectJSON getMetadata() {
        ObjectJSON metadata = new ObjectJSON();
        metadata.setString("semver", "3.0.0");
        metadata.setString("vm", "0.2.0-prerelease.20220222132735");
        metadata.setString("agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Scratch/3.29.1 Chrome/94.0.4606.81 Electron/15.3.1 Safari/537.36");
        return metadata;
    }
}
