import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MusicalInformationPanel extends JPanel {
    private static DefaultListModel<Musical> musicalListModel;
    private JList<Musical> musicalList;
    private JTextField searchField;

    public MusicalInformationPanel() {
        setLayout(new BorderLayout());

        musicalListModel = new DefaultListModel<>();
        musicalList = new JList<>(musicalListModel);
        musicalList.setCellRenderer(new MusicalCellRenderer());

        JScrollPane scrollPane = new JScrollPane(musicalList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Add search bar
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterMusicals(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterMusicals(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });

        add(searchField, BorderLayout.NORTH);

        populateMusicalsFromCSV("musicals.csv"); // Change the filename accordingly
    }

    private void filterMusicals(String searchText) {
        DefaultListModel<Musical> filteredModel = new DefaultListModel<>();
        for (int i = 0; i < musicalListModel.getSize(); i++) {
            Musical musical = musicalListModel.getElementAt(i);
            if (musical.name.toLowerCase().contains(searchText.toLowerCase())) {
                filteredModel.addElement(musical);
            }
        }
        musicalList.setModel(filteredModel);
    }

    private void populateMusicalsFromCSV(String fileName) {
        try {
            Path path = Paths.get(fileName);
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    musicalListModel.addElement(new Musical(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MusicalCellRenderer extends JPanel implements ListCellRenderer<Musical> {
        private JLabel imageLabel;
        private JPanel detailsPanel;

        private JLabel nameLabel;
        private JLabel runTimeLabel;
        private JLabel categoriesLabel;
        private JLabel venueLabel;
        private JLabel ageLabel;

        public MusicalCellRenderer() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setBackground(Color.WHITE);

            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            add(imageLabel, BorderLayout.WEST);

            detailsPanel = new JPanel(new GridLayout(5, 1));
            detailsPanel.setBackground(Color.WHITE);

            nameLabel = createLabel();
            runTimeLabel = createLabel();
            categoriesLabel = createLabel();
            venueLabel = createLabel();
            ageLabel = createLabel();

            detailsPanel.add(nameLabel);
            detailsPanel.add(runTimeLabel);
            detailsPanel.add(categoriesLabel);
            detailsPanel.add(venueLabel);
            detailsPanel.add(ageLabel);

            detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

            add(detailsPanel, BorderLayout.CENTER);
        }

        private JLabel createLabel() {
            JLabel label = new JLabel();
            label.setForeground(Color.DARK_GRAY);
            return label;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Musical> list, Musical value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            ImageIcon icon = new ImageIcon(getClass().getResource(value.imagePath));
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH)));

            nameLabel.setText("<html><h2>" + value.name + "</h2></html>");
            runTimeLabel.setText("<html><i>Run Time:</i> " + value.runTime + "</html>");
            categoriesLabel.setText("<html><b>Categories:</b> " + value.categories + "</html>");
            venueLabel.setText("<html><b>Venue:</b> " + value.venue + "</html>");
            ageLabel.setText("<html><b>Age:</b> " + value.age + "</html>");

            setBackground(isSelected ? list.getSelectionBackground() : Color.WHITE);
            setForeground(isSelected ? list.getSelectionForeground() : Color.DARK_GRAY);
            return this;
        }
    }

    private static class Musical {
        private String name;
        private String runTime;
        private String categories;
        private String venue;
        private String age;
        private String imagePath;

        public Musical(String name, String runTime, String categories, String venue, String age, String imagePath) {
            this.name = name;
            this.runTime = runTime;
            this.categories = categories;
            this.venue = venue;
            this.age = age;
            this.imagePath = imagePath;
        }

        @Override
        public String toString() {
            return String.format("%d. %s\nRun Time: %s\nCategories: %s\nVenue: %s\nAge: %s",
                    musicalListModel.size() + 1, name, runTime, categories, venue, age);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Musical Information");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            MusicalInformationPanel musicalInformationPanel = new MusicalInformationPanel();
            frame.add(musicalInformationPanel);

            frame.setVisible(true);
        });
    }
}
