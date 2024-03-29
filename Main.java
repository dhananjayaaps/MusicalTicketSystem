import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;

public class Main extends JFrame {
    private JPanel buttonPanel;
    private JButton musicalListButton;
    private JButton showScheduleButton;
    private JButton bookTicketButton;
    private JButton exitButton;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private InformationPanel informationPanel;
    private SchedulePanel showSchedulePanel;
    private BookTicketPanel bookTicketPanel;

    private Clip backgroundClip;

    public Main() {
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

        informationPanel = new InformationPanel();
        contentPanel.add(informationPanel, "MusicalInformation");

        showSchedulePanel = new SchedulePanel();
        contentPanel.add(showSchedulePanel, "ShowSchedule");

        bookTicketPanel = new BookTicketPanel();
        contentPanel.add(bookTicketPanel, "BookTicket");

        add(buttonPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        makeMusic("music.wav");

        setupActionListeners();
    }

    private void makeMusic(String musicFilePath) {
        try {
            File audioFile = new File(musicFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupActionListeners() {
        musicalListButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "MusicalInformation");
            playBackgroundMusic();
        });

        showScheduleButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "ShowSchedule");
            playBackgroundMusic();
        });

        bookTicketButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "BookTicket");
            playBackgroundMusic();
        });

        exitButton.addActionListener(e -> {
            stopBackgroundMusic();
            System.exit(0);
        });
    }

    private void playBackgroundMusic() {
        if (backgroundClip != null && !backgroundClip.isRunning()) {
            backgroundClip.start();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main gui = new Main();
            gui.setVisible(true);
        });
    }
}
