import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class QuizApp {
    private JFrame frame;
    private JPanel panel;
    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup optionGroup;
    private JButton submitButton;
    private javax.swing.Timer timer; 
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int totalQuestions = 5;
    private java.util.List<Question> questions;
    private JLabel timerLabel;
    private int remainingTime = 10; 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizApp().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Quiz Application");
        panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10)); 

     
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));

    
        options = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            optionsPanel.add(options[i]);
            optionGroup.add(options[i]);
        }

       
        submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(this::handleSubmit);

      
        timerLabel = new JLabel("Time left: 10", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

      
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(questionLabel, BorderLayout.CENTER);
        topPanel.add(timerLabel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(optionsPanel);
        bottomPanel.add(submitButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);

      
        frame.add(panel);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        loadQuestions();
        showNextQuestion();
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("What is the capital of France?", 
            new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 2));
        questions.add(new Question("Which planet is known as the Red Planet?", 
            new String[]{"Earth", "Mars", "Jupiter", "Saturn"}, 1));
        questions.add(new Question("Who developed the theory of relativity?", 
            new String[]{"Newton", "Einstein", "Galileo", "Bohr"}, 1));
        questions.add(new Question("Which animal is known as the King of the Jungle?", 
            new String[]{"Lion", "Tiger", "Elephant", "Cheetah"}, 0));
        questions.add(new Question("What is the largest ocean on Earth?", 
            new String[]{"Atlantic", "Indian", "Arctic", "Pacific"}, 3));
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < totalQuestions) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestionText());

            String[] currentOptions = currentQuestion.getOptions();
            for (int i = 0; i < currentOptions.length; i++) {
                options[i].setText(currentOptions[i]);
            }

            startTimer();
        } else {
            showResults();
        }
    }

    private void startTimer() {
        remainingTime = 10;
        timerLabel.setText("Time left: " + remainingTime);
        if (timer != null) {
            timer.stop();
        }
        timer = new javax.swing.Timer(1000, e -> {
            remainingTime--;
            timerLabel.setText("Time left: " + remainingTime);
            if (remainingTime <= 0) {
                handleSubmit(null);
            }
        });
        timer.start();
    }

    private void handleSubmit(ActionEvent e) {
        if (timer != null) {
            timer.stop();
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        int selectedOptionIndex = getSelectedOptionIndex();

        if (selectedOptionIndex != -1 && selectedOptionIndex == currentQuestion.getCorrectAnswerIndex()) {
            score++;
        }

        currentQuestionIndex++;
        showNextQuestion();
    }

    private int getSelectedOptionIndex() {
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                return i;
            }
        }
        return -1; 
    }

    private void showResults() {
        panel.removeAll();

        JLabel resultLabel = new JLabel("Your Score: " + score + "/" + totalQuestions, JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));

        panel.add(resultLabel, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    private static class Question {
        private String questionText;
        private String[] options;
        private int correctAnswerIndex;

        public Question(String questionText, String[] options, int correctAnswerIndex) {
            this.questionText = questionText;
            this.options = options;
            this.correctAnswerIndex = correctAnswerIndex;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String[] getOptions() {
            return options;
        }

        public int getCorrectAnswerIndex() {
            return correctAnswerIndex;
        }
    }
}
