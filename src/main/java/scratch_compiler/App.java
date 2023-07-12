package scratch_compiler;

public class App {
    public static void main(String[] args) throws Exception {
        // Block block = new Block("motion_ifonedgebounce", null, null);
        // System.out.println(block.toJSON());
        String file = ZipUtils.readZipFile("Mitt Scratch-prosjekt2 (5).sb3", "project.json");
        System.out.println(file);
    }
}
