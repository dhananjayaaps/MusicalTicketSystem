import javax.swing.*;
import java.awt.*;

public class MusicalInformationPanel extends JPanel {
    private JList<Musical> musicalList;
    private static DefaultListModel<Musical> musicalListModel;

    public MusicalInformationPanel() {
        setLayout(new BorderLayout());
        musicalListModel = new DefaultListModel<>();
        musicalList = new JList<>(musicalListModel);
        musicalList.setCellRenderer(new MusicalCellRenderer());
        populateMusicals();
        add(new JScrollPane(musicalList), BorderLayout.CENTER);
    }

    private void populateMusicals() {
        musicalListModel.addElement(new Musical("The Lion King", "2h 30m (incl. interval)",
                "Disney Shows, Family and Kids, Last Minute Tickets", "Lyceum Theatre",
                "6+ Children Under 3 are not permitted to enter the theatre"));

        musicalListModel.addElement(new Musical("Les Misérables", "3h (incl. interval)", "Drama, Musicals",
                "Queen's Theatre", "8+"));

        musicalListModel.addElement(new Musical("Wicked", "2h 45m (incl. interval)", "Musicals",
                "Apollo Victoria Theatre", "5+"));
    }

    private static class MusicalCellRenderer extends JPanel implements ListCellRenderer<Musical> {
        private JLabel nameLabel;
        private JLabel runTimeLabel;
        private JLabel categoriesLabel;
        private JLabel venueLabel;
        private JLabel ageLabel;

        public MusicalCellRenderer() {
            setLayout(new GridLayout(5, 1));
            nameLabel = new JLabel();
            runTimeLabel = new JLabel();
            categoriesLabel = new JLabel();
            venueLabel = new JLabel();
            ageLabel = new JLabel();

            add(nameLabel);
            add(runTimeLabel);
            add(categoriesLabel);
            add(venueLabel);
            add(ageLabel);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Musical> list, Musical value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            if (value.name.isEmpty()) {
                setPreferredSize(new Dimension(0, 10));
                setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                return this;
            }

            nameLabel.setText(value.name);
            runTimeLabel.setText("Run Time: " + value.runTime);
            categoriesLabel.setText("Categories: " + value.categories);
            venueLabel.setText("Venue: " + value.venue);
            ageLabel.setText("Age: " + value.age);

            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return this;
        }
    }

    private static class Musical {
        private String name;
        private String runTime;
        private String categories;
        private String venue;
        private String age;

        public Musical(String name, String runTime, String categories, String venue, String age) {
            this.name = name;
            this.runTime = runTime;
            this.categories = categories;
            this.venue = venue;
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("%d. %s\nRun Time: %s\nCategories: %s\nVenue: %s\nAge: %s",
                    musicalListModel.size() + 1, name, runTime, categories, venue, age);
        }
    }
}
