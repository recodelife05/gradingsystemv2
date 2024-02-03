import javax.sound.midi.SysexMessage;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;


//TODO: removed is not yet working in computation.
public class GradingPage extends JFrame {

    JLabel totalActivityLabel = new JLabel();
    JLabel totalLabLabel = new JLabel();
    JLabel totalExamLabel = new JLabel();
    JLabel finalGradeLabel = new JLabel();
    JTextField activityTextField = new JTextField();
    DefaultListModel<Double> activityGrades = new DefaultListModel<>();
    DefaultListModel<Double> labGrades = new DefaultListModel<>();
    DefaultListModel<Double> examGrades = new DefaultListModel<>();
    JButton activityComputeBtn = new JButton("Add Compute Activity");
    JButton examComputeBtn = new JButton("Add Exam Score");
    JButton labComputeBtn = new JButton("Add Lab Score");
    JButton finalGradeBtn = new JButton("Compute Final Grade");
    JButton removedRecordsBtn = new JButton("Removed records");
    JTable activityTable = new JTable();
    DefaultTableModel model = new DefaultTableModel();
    String ACTIVITYGRADE = "Activity Grade";
    String LABGRADE = "Laboratory Grade";
    String EXAMGRADE = "Exam Grade";
    String FINALEGRADE = "Final Grade";
    String[][] data = { };
    double TotalActivity = 0;
    double TotalExam = 0;
    double TotalLab = 0;

    GradingPage(){
        InitializedComponents();
        SetEvents();
    }
    public JTextField[] activityScores = new JTextField[10];
    public void InitializedComponents(){
        this.setTitle("Grading System");
        this.setSize(500,500);
        this.setLayout(new GridLayout(1, 3, 10, 10));
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(new Rectangle(500,500));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        String[] columnNames = {"Select","Grade Type", "Score"};

        //set select
         model = new DefaultTableModel() {
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Boolean.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    default:
                        return  String.class;
                }
            }
        };

        activityTable.setModel(model);
        activityTable = new JTable(model);
        activityTable.setBounds(30, 40, 200, 300);

        Arrays.stream(columnNames).forEach(f -> {
            model.addColumn(f);
        });

        mainPanel.add(activityComputeBtn);
        mainPanel.add(labComputeBtn);
        mainPanel.add(examComputeBtn);
        mainPanel.add(finalGradeBtn);
        mainPanel.add(removedRecordsBtn);
        mainPanel.add(totalActivityLabel);
        mainPanel.add(totalLabLabel);
        mainPanel.add(totalExamLabel);
        mainPanel.add(finalGradeLabel);
        mainPanel.add(activityTextField);

        mainPanel.add(new JScrollPane(activityTable));

        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(mainPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }
    private void SetEvents(){
        activityComputeBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                TotalActivity = 0;
                Double currentScore = Double.parseDouble(activityTextField.getText());

                model.addRow(new Object[] {false, ACTIVITYGRADE,currentScore.toString() });
                activityGrades.add(0,currentScore);
                activityGrades.elements().asIterator()
                        .forEachRemaining(r ->
                                        TotalActivity += r.doubleValue()
                                );
                System.out.println("activity grades count" + activityGrades.getSize());
                totalActivityLabel.setText("Total " + ACTIVITYGRADE + ":" + TotalActivity / activityGrades.getSize());
            }
        });

        labComputeBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                TotalLab = 0;
                Double currentScore = Double.parseDouble(activityTextField.getText());
                model.addRow(new Object[] {false,LABGRADE,currentScore.toString()});
                labGrades.add(0,currentScore);
                labGrades.elements().asIterator()
                        .forEachRemaining(r ->
                                TotalLab +=  r.doubleValue()
                        );

                totalLabLabel.setText(String.format("Total " + LABGRADE + ": %.2f", TotalLab / labGrades.getSize()));

            }
        });

        examComputeBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                TotalExam = 0;
                Double currentScore = Double.parseDouble(activityTextField.getText());
                model.addRow(new Object[] {false,EXAMGRADE,currentScore.toString()});
                examGrades.add(0,currentScore);
                examGrades.elements().asIterator()
                        .forEachRemaining(r ->
                                TotalExam +=  r.doubleValue()
                        );

                totalExamLabel.setText(String.format("Total " + EXAMGRADE + ": %.2f", TotalExam / examGrades.getSize()));
            }
        });

        finalGradeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ComputeFinalGrade();
            }
        });

        removedRecordsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    for(int i = 0; i < activityTable.getRowCount(); i++){

                        Boolean checked = Boolean.valueOf(activityTable.getValueAt(i,0).toString());
                        String col  = activityTable.getValueAt(i,1).toString();
                        System.out.println(checked);
                        if(checked){
                            System.out.println(i);
                            ((DefaultTableModel) activityTable.getModel()).removeRow(i);
                            //TODO: should not refer on the List for computation.
                        }

                    }
            }
        });
    }
    public void ComputeFinalGrade(){

        //exam * 40, lab 30percent , activity .30
        System.out.println("total lab: " + (TotalLab/ labGrades.getSize()) * 0.30);
        System.out.println("total exam: " + ((TotalExam / examGrades.getSize()) * 0.40));
        System.out.println("total activity: " +((TotalActivity / activityGrades.getSize()) * 0.30));
        double finalGrade = ((TotalLab/ labGrades.getSize()) * 0.30) +
                ((TotalActivity / activityGrades.getSize()) * 0.30) +
                ((TotalExam / examGrades.getSize()) * 0.40);
        finalGradeLabel.setText(String.format(FINALEGRADE + ": %.2f", finalGrade));
        if (finalGrade < 70) {
            finalGradeLabel.setForeground(Color.RED);
        } else {
            finalGradeLabel.setForeground(Color.BLACK);
        }
    }



}
