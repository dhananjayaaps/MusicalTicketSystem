import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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

    private Clip backgroundMusic;

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

        initializeBackgroundMusic();

        setupActionListeners();
    }

    private void initializeBackgroundMusic() {
        try {
            // Load the music file
            File musicFile = new File("background_music.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);

            // Get a Clip object to play the music
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);

            // Loop the music indefinitely
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playBackgroundMusic() {
        // Start playing the background music
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.start();
        }
    }

    private void stopBackgroundMusic() {
        // Stop the background music
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            LondonMusicalTicketSystemGUI gui = new LondonMusicalTicketSystemGUI();
            gui.setVisible(true);
        });
    }
}
