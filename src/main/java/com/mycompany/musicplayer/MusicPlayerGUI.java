package com.mycompany.musicplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MusicPlayerGUI extends JFrame {
    private boolean isLiked = false, isShuffled = false, isPlaying = false, isRepeating = false;
    private JLabel songTitle, currentTimeLabel, totalTimeLabel;
    private JSlider progressSlider;
    private Timer progressTimer;
    private JButton shuffleButton, likeButton, previousButton, playButton, nextButton, repeatButton;
    private MusicControls musicControls;
    private Playlist currentPlaylist;
    private Song currentSong;

    public MusicPlayerGUI() {
        setTitle("Simple Music Player");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 20));

        musicControls = new MusicControls();
        currentPlaylist = new Playlist("Default Playlist");
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        // Add song end listener
        musicControls.addSongEndListener(() -> {
            if (isRepeating) {
                SwingUtilities.invokeLater(() -> {
                    musicControls.stop();
                    musicControls.play();
                });
            } else {
                SwingUtilities.invokeLater(this::nextTrack);
            }
        });
    }

    private void initializeComponents() {
        songTitle = new JLabel("No song playing", JLabel.CENTER);
        songTitle.setFont(new Font("Arial", Font.BOLD, 16));
        currentTimeLabel = new JLabel("0:00");
        currentTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        totalTimeLabel = new JLabel("0:00");
        totalTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        progressSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        progressSlider.setPreferredSize(new Dimension(300, 40));
        progressTimer = new Timer(100, e -> updateProgress());
    }

    private void setupLayout() {
        // NORTH: Song title and image
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        topPanel.add(songTitle, BorderLayout.NORTH);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(300, 300));
        imagePanel.setBackground(Color.LIGHT_GRAY);

        java.net.URL imageUrl = getClass().getResource("/record.png");
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaled = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            imagePanel.add(new JLabel(new ImageIcon(scaled), JLabel.CENTER), BorderLayout.CENTER);
        }

        topPanel.add(imagePanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // CENTER: Slider and time labels
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        centerPanel.add(progressSlider, BorderLayout.CENTER);

        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        timePanel.add(currentTimeLabel, BorderLayout.WEST);
        timePanel.add(totalTimeLabel, BorderLayout.EAST);
        centerPanel.add(timePanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // SOUTH: Control buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        shuffleButton = createButton("🔀", 40, 40, 18);
        repeatButton = createButton("🔁", 40, 40, 18);
        likeButton = new JButton("♡");
        likeButton.setPreferredSize(new Dimension(40, 40));
        likeButton.setFont(new Font("Dialog", Font.PLAIN, 25));
        likeButton.setForeground(Color.GRAY);
        likeButton.setFocusPainted(false);
        likeButton.setBorderPainted(false);
        likeButton.setContentAreaFilled(false);
        likeButton.setMargin(new Insets(-3, 0, 0, 0));
        likeButton.setOpaque(false);
        likeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        likeButton.setVerticalAlignment(SwingConstants.TOP);
        likeButton.setHorizontalAlignment(SwingConstants.CENTER);
        previousButton = createButton("⏮", 40, 40, 18);
        playButton = createButton("▶", 40, 40, 18);
        nextButton = createButton("⏭", 40, 40, 18);

        for (JButton button : new JButton[]{shuffleButton, likeButton, previousButton, playButton, nextButton, repeatButton}) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    updateButtonColor(button, true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    updateButtonColor(button, false);
                }
            });
        }

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        volumeSlider.setPreferredSize(new Dimension(200, 20));
        volumeSlider.addChangeListener(e -> {
            if (!volumeSlider.getValueIsAdjusting()) {
                musicControls.setVolume(volumeSlider.getValue() / 100.0f);
            }
        });

        controlPanel.add(shuffleButton);
        controlPanel.add(previousButton);
        controlPanel.add(playButton);
        controlPanel.add(nextButton);
        controlPanel.add(repeatButton);

        JPanel volumePanel = new JPanel();
        volumePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JLabel volumeLabel = new JLabel("🔊");
        volumeLabel.setPreferredSize(new Dimension(50, 50));
        volumeLabel.setFont(new Font("Dialog", Font.PLAIN, 17));
        volumePanel.add(volumeLabel);
        volumePanel.add(volumeSlider);

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.add(controlPanel, BorderLayout.CENTER);
        bottomContainer.add(volumePanel, BorderLayout.SOUTH);
        add(bottomContainer, BorderLayout.SOUTH);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem playlistItem = new JMenuItem("Playlist");
        fileMenu.add(openItem);
        fileMenu.add(playlistItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Button Actions
        shuffleButton.addActionListener(e -> toggleShuffle());
        repeatButton.addActionListener(e -> toggleRepeat());
        likeButton.addActionListener(e -> toggleLike());
        previousButton.addActionListener(e -> previousTrack());
        playButton.addActionListener(e -> togglePlay());
        nextButton.addActionListener(e -> nextTrack());
        openItem.addActionListener(e -> openFile());
        playlistItem.addActionListener(e -> showPlaylist());
        progressSlider.addChangeListener(e -> seek());
    }

    private JButton createButton(String text, int width, int height, int fontSize) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.GRAY);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, fontSize));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void updateButtonColor(JButton button, boolean isHover) {
        if (button == likeButton && isLiked) {
            button.setForeground(Color.RED);
        } else if (button == shuffleButton && isShuffled) {
            button.setForeground(new Color(30, 215, 96));
        } else if (button == repeatButton && isRepeating) {
            button.setForeground(new Color(30, 215, 96));
        } else {
            button.setForeground(isHover ? new Color(30, 215, 96) : Color.GRAY);
        }
    }

    private void toggleShuffle() {
        isShuffled = !isShuffled;
        shuffleButton.setForeground(isShuffled ? new Color(30, 215, 96) : Color.GRAY);
        if (isShuffled) {
            currentPlaylist.shuffle();
            if (currentSong != null) {
                currentPlaylist.getSongs().remove(currentSong);
                currentPlaylist.getSongs().add(0, currentSong);
            }
        }
    }

    private void toggleRepeat() {
        isRepeating = !isRepeating;
        musicControls.setRepeating(isRepeating);
        repeatButton.setForeground(isRepeating ? new Color(30, 215, 96) : Color.GRAY);
        if (isRepeating) {
            repeatButton.setText("🔂");
        } else {
            repeatButton.setText("🔁");
        }
    }

    private void toggleLike() {
        isLiked = !isLiked;
        likeButton.setText(isLiked ? "❤" : "♡");
        likeButton.setForeground(isLiked ? Color.RED : Color.GRAY);
        if (currentSong != null) {
            currentSong.setLiked(isLiked);
        }
    }

    private void togglePlay() {
        if (currentSong == null) {
            openFile();
            return;
        }
        if (isPlaying) {
            musicControls.pause();
            progressTimer.stop();
        } else {
            musicControls.play();
            progressTimer.start();
        }
        isPlaying = !isPlaying;
        playButton.setText(isPlaying ? "⏸" : "▶");
        songTitle.setText(isPlaying ? currentSong.toString() : "No song playing");
    }

    private void previousTrack() {
        if (currentPlaylist.getSize() > 0) {
            int currentIndex = currentPlaylist.getSongs().indexOf(currentSong);
            int prevIndex = (currentIndex - 1 + currentPlaylist.getSize()) % currentPlaylist.getSize();
            playSong(currentPlaylist.getSong(prevIndex));
        }
    }

    private void nextTrack() {
        if (currentPlaylist.getSize() > 0) {
            int currentIndex = currentPlaylist.getSongs().indexOf(currentSong);
            int nextIndex = (currentIndex + 1) % currentPlaylist.getSize();
            playSong(currentPlaylist.getSong(nextIndex));
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Audio Files", "wav", "mp3", "aiff"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();
            int lastDot = fileName.lastIndexOf('.');
            String title = lastDot > 0 ? fileName.substring(0, lastDot) : fileName;
            Song song = new Song(title, "Unknown Artist", "Unknown Album", file, 0);
            currentPlaylist.addSong(song);
            playSong(song);
        }
    }

    private void playSong(Song song) {
        try {
            currentSong = song;
            musicControls.loadSong(song.getFile());
            songTitle.setText(song.toString());
            
            progressSlider.setValue(0);
            currentTimeLabel.setText("0:00");
            totalTimeLabel.setText(formatTime(musicControls.getTotalDuration()));
            
            musicControls.play();
            isPlaying = true;
            playButton.setText("⏸");
            progressTimer.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error playing file: " + e.getMessage());
            isPlaying = false;
            playButton.setText("▶");
        }
    }

    private void updateProgress() {
        if (musicControls != null && isPlaying) {
            long current = musicControls.getCurrentPosition();
            long total = musicControls.getTotalDuration();
            if (total > 0) {
                currentTimeLabel.setText(formatTime(current));
                
                int progress = (int) ((current * 100) / total);
                if (progress <= 100 && !progressSlider.getValueIsAdjusting()) {
                    progressSlider.setValue(progress);
                }
            }
        }
    }

    private String formatTime(long microseconds) {
        long seconds = microseconds / 1_000_000;
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    private void seek() {
        if (!progressSlider.getValueIsAdjusting() && currentSong != null) {
            long total = musicControls.getTotalDuration();
            long position = (progressSlider.getValue() * total) / 100;
            musicControls.seek(position);
            currentTimeLabel.setText(formatTime(position));
            totalTimeLabel.setText(formatTime(total));
        }
    }

    private void showPlaylist() {
        JDialog dialog = new JDialog(this, "Playlist", true);
        dialog.setLayout(new BorderLayout());
        DefaultListModel<Song> model = new DefaultListModel<>();
        currentPlaylist.getSongs().forEach(model::addElement);
        JList<Song> list = new JList<>(model);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Song selected = list.getSelectedValue();
                    if (selected != null) {
                        playSong(selected);
                        dialog.dispose();
                    }
                }
            }
        });
        dialog.add(new JScrollPane(list), BorderLayout.CENTER);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    
    private void setupEventListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (progressTimer != null) progressTimer.stop();
                if (musicControls != null) musicControls.cleanup();
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MusicPlayerGUI().setVisible(true));
    }
}