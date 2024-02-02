import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GradingPage extends JFrame {

    JLabel totalActivity = new JLabel();
    JTextField activityTextField = new JTextField();
    DefaultListModel<Integer> activityGrades = new DefaultListModel<>();

    JButton activityComputeBtn = new JButton("Compute Activity");
    Integer TotalActivity = 0;
    GradingPage(){
        InitializedComponents();
        SetEvents();
    }
    public JTextField[] activityScores = new JTextField[10];
    public void InitializedComponents(){
        this.setTitle("Grading System");
        this.setSize(500,500);
        this.setLayout(new GridLayout(1, 3, 10, 10));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBounds(new Rectangle(500,500));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        activityGrades.add(0,60);
        activityGrades.add(1,90);
        JList<Integer> ac = new JList<>(activityGrades);

        MyCellRenderer renderer = new MyCellRenderer();
        ac.setCellRenderer(renderer);
        ac.setToolTipText("Activity Grades");
        ac.setVisible(true);

        ac.setSize(200,200);
        bottomPanel.add(ac);
        bottomPanel.add(activityComputeBtn);
        bottomPanel.add(totalActivity);
        bottomPanel.add(activityTextField);


        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }
    public class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {
        public MyCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {

            setText(value.toString());

            Color background;
            Color foreground;

            // check if this cell represents the current DnD drop location
            JList.DropLocation dropLocation = list.getDropLocation();
            if (dropLocation != null
                    && !dropLocation.isInsert()
                    && dropLocation.getIndex() == index) {

                background = Color.BLUE;
                foreground = Color.WHITE;

                // check if this cell is selected
            } else if (isSelected) {
                background = Color.GREEN;
                foreground = Color.WHITE;

                // unselected, and not the DnD drop location
            } else {
                background = Color.WHITE;
                foreground = Color.BLACK;
            };

            setBackground(background);
            setForeground(foreground);

            return this;
        }
    }
    private void SetEvents(){
        activityComputeBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                TotalActivity = 0;
                Integer currentScore = Integer.parseInt(activityTextField.getText());
                activityGrades.add(0,currentScore);

                activityGrades.elements().asIterator()
                        .forEachRemaining(r ->
                                        TotalActivity +=  r.intValue()
                                );

                totalActivity.setText(TotalActivity.toString());
            }
        });
    }

    public void ComputeActivity(){

    }
    public class ActivityGrade {

        public int Score;
    }

    public void AddActivityScore(){

    }
}
