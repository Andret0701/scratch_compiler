package scratch_compiler;

public class App {
    public static void main(String[] args) throws Exception {
        // Block block = new Block("motion_ifonedgebounce", null, null);
        // System.out.println(block.toJSON());

        ScratchProject project = new ScratchProject();
        String file = ZipUtils.readZipFile("Mitt Scratch-prosjekt2 (8).sb3", "project.json");
        System.out.println(file);

        ZipUtils.writeZipFile("Mitt Scratch-prosjekt2 (8).sb3", "project.json", project.toJSON());

    }
}
