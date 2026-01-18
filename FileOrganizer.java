import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class FileOrganizer {

    // A dictionary to map file extensions to specific folder names
    private static final Map<String, String> FOLDER_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        // Images
        map.put("jpg", "Images"); map.put("jpeg", "Images"); map.put("png", "Images"); map.put("gif", "Images");
        // Documents
        map.put("pdf", "Documents"); map.put("docx", "Documents"); map.put("txt", "Documents");
        // Programming/Code
        map.put("java", "Code"); map.put("cpp", "Code"); map.put("py", "Code"); map.put("html", "Code");
        // Others
        map.put("zip", "Archives"); map.put("mp4", "Videos");
        FOLDER_MAP = Collections.unmodifiableMap(map);
    }

    public static void main(String[] args) {
        // If a path is typed in the terminal, run CLI mode immediately
        if (args.length > 0) {
            System.out.println(">>> CLI Mode Active. Organizing: " + args[0]);
            runAutomation(Paths.get(args[0]), null);
        } 
        // If NO path is typed (like in your screenshot), launch the GUI Window
        else {
            System.out.println(">>> Launching GUI Window...");
            SwingUtilities.invokeLater(FileOrganizer::createAndShowGUI);
        }
    }

    // --- GUI VIEW ---
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Smart Student File Organizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout(15, 15));

        JTextArea logView = new JTextArea("Ready to organize...\n");
        logView.setEditable(false);
        logView.setMargin(new Insets(10,10,10,10));
        
        JButton selectButton = new JButton("Select Folder to Clean Up");
        selectButton.setPreferredSize(new Dimension(0, 50));

        selectButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                Path path = chooser.getSelectedFile().toPath();
                logView.append("\nStarting organization in: " + path + "\n---\n");
                runAutomation(path, logView);
                logView.append("---\nCompleted Successfully!\n");
            }
        });

        frame.add(selectButton, BorderLayout.NORTH);
        frame.add(new JScrollPane(logView), BorderLayout.CENTER);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    // --- SHARED CORE LOGIC 
    private static void runAutomation(Path dir, JTextArea optionalLog) {
        try (Stream<Path> files = Files.list(dir)) {
            files.filter(Files::isRegularFile).forEach(file -> {
                String name = file.getFileName().toString();
                String ext = getExt(name);
                String folderName = FOLDER_MAP.getOrDefault(ext, "Others");
                
                Path targetDir = dir.resolve(folderName);
                try {
                    if (Files.notExists(targetDir)) Files.createDirectory(targetDir);
                    Files.move(file, targetDir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
                    
                    String status = "Moved: " + name + " -> " + folderName;
                    if (optionalLog != null) optionalLog.append(status + "\n");
                    else System.out.println(status);
                } catch (IOException ex) {
                    if (optionalLog != null) optionalLog.append("Error moving " + name + "\n");
                }
            });
        } catch (IOException e) {
            System.err.println("Could not access directory.");
        }
    }

    private static String getExt(String name) {
        int i = name.lastIndexOf('.');
        return (i == -1) ? "" : name.substring(i + 1).toLowerCase();
    }
}