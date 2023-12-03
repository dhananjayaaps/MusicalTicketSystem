import javax.swing.*;
import java.awt.*;

public class LondonMusicalTicketSystemGUI extends JFrame {
    private JPanel buttonPanel;
    private JButton musicalListButton;
    private JButton showScheduleButton;
    private JButton bookTicketButton;
    private JButton exitButton;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private MusicalInformationPanel musicalInformationPanel;
    private ShowSchedulePanel showSchedulePanel;
    private BookTicketPanel bookTicketPanel;

    public LondonMusicalTicketSystemGUI() {
        setTitle("London Musical Ticket System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        buttonPanel = new JPanel(new FlowLayout());
        musicalListButton = new JButton("Musical List");
        showScheduleButton = new JButton("Show Schedule");
        bookTicketButton = new JButton("Book Ticket");
        exitButton = new JButton("Exit");

        buttonPanel.add(musicalListButton);
        buttonPanel.add(showScheduleButton);
        buttonPanel.add(bookTicketButton);
        buttonPanel.add(exitButton);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        musicalInformationPanel = new MusicalInformationPanel();
        contentPanel.add(musicalInformationPanel, "MusicalInformation");

        showSchedulePanel = new ShowSchedulePanel();
        contentPanel.add(showSchedulePanel, "ShowSchedule");

        bookTicketPanel = new BookTicketPanel();
        contentPanel.add(bookTicketPanel, "BookTicket");

        add(buttonPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setupActionListeners();
    }

    private void setupActionListeners() {
        musicalListButton.addActionListener(e -> cardLayout.show(contentPanel, "MusicalInformation"));

        showScheduleButton.addActionListener(e -> cardLayout.show(contentPanel, "ShowSchedule"));

        bookTicketButton.addActionListener(e -> cardLayout.show(contentPanel, "BookTicket"));

        exitButton.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LondonMusicalTicketSystemGUI gui = new LondonMusicalTicketSystemGUI();
            gui.setVisible(true);
        });
    }
}
