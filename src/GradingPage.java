import javax.sound.midi.SysexMessage;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

import static java.util.spi.ToolProvider.findFirst;


//TODO: removed is not yet working in computation.
public class GradingPage extends JFrame {

    int TOTALRECORDS = 0;
    double EXAM_PERCENTAGE = 0.40;
    double ACTIVITYANDLAB_PERCENTAGE = 0.30;
    JLabel totalActivityLabel = new JLabel();
    JLabel totalLabLabel = new JLabel();
    JLabel totalExamLabel = new JLabel();
    JLabel finalGradeLabel = new JLabel();

    String CurrentSelectedActivity = GradeType.ACTIVITY.toString();
    GradeType CurrentSelectAct = GradeType.ACTIVITY;

    JComboBox activityComboBox = new JComboBox();
    JTextField activityTextField = new JTextField();
    DefaultListModel<Double> activityGrades = new DefaultListModel<>();
    DefaultListModel<Double> labGrades = new DefaultListModel<>();
    DefaultListModel<Double> examGrades = new DefaultListModel<>();
    JButton activityComputeBtn = new JButton("Add Compute Activity");
    JButton examComputeBtn = new JButton("Add Exam Score");
    JButton labComputeBtn = new JButton("Add Lab Score");
    JButton finalGradeBtn = new JButton("Compute Final Grade");
    JButton addRecordBtn = new JButton("Add Record");
    JButton removedRecordsBtn = new JButton("Removed records");
    JTable activityTable = new JTable();
    //0-3
    String[] columnNames = {"Select","Grade Type", "Score","Id"};
    ArrayList<GradeRecord> grades = new ArrayList<>();
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
    public void InitializedComponents(){
        this.setTitle("Grading System");
        this.setSize(500,500);
        this.setLayout(new GridLayout(1, 3, 10, 10));
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(new Rectangle(500,500));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));




        String selectActivity[] = { GradeType.ACTIVITY.toString(),GradeType.LAB.toString(),GradeType.EXAM.toString() };
        //set combobox
        activityComboBox = new JComboBox(selectActivity);
        activityComboBox.setSize(new Dimension(20,20));
        mainPanel.add(activityComboBox);

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
        /*
        mainPanel.add(activityComputeBtn);
        mainPanel.add(labComputeBtn);
        mainPanel.add(examComputeBtn);
        */

        JPanel panelSummary = new JPanel(new GridLayout(3, 2));
        panelSummary.setLayout(new BoxLayout(panelSummary, BoxLayout.Y_AXIS));
        panelSummary.setBorder(BorderFactory.createTitledBorder("Grade Summary"));


        mainPanel.add(activityTextField);
        mainPanel.add(addRecordBtn);

        /*
        TODO: add more design on log in page and set alignments
        panelSummary.add(totalActivityLabel);
        panelSummary.add(totalLabLabel);
        panelSummary.add(totalExamLabel);
        panelSummary.add(finalGradeLabel);
        mainPanel.add(panelSummary);
        */
        mainPanel.add(totalActivityLabel);
        mainPanel.add(totalLabLabel);
        mainPanel.add(totalExamLabel);
        mainPanel.add(finalGradeLabel);

        mainPanel.add(new JScrollPane(activityTable));

        mainPanel.add(removedRecordsBtn);
        mainPanel.add(finalGradeBtn);


        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(mainPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }
    private void SetEvents(){

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
                        String gradeIdToRemoved  = activityTable.getValueAt(i,3).toString();
                        System.out.println(checked);
                        if(checked){
                            System.out.println(i);
                            ((DefaultTableModel) activityTable.getModel()).removeRow(i);

                            Predicate<GradeRecord> lambda = r->r.GradeId.equals(gradeIdToRemoved);
                            Integer indexToRemoved = Integer.parseInt(gradeIdToRemoved);
                            var item = grades.stream().filter(r->r.GradeId.equals(indexToRemoved.intValue())).findFirst();
                            grades.remove(item.get());
                            //TODO: should not refer on the List for computation.
                        }

                    }
            }
        });

        addRecordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var totalExam = grades.stream().filter(r->r.Type.equals(GradeType.EXAM)).map(r->r.Score).reduce(0.00,Double::sum);
                var countExam = grades.stream().filter(r->r.Type.equals(GradeType.EXAM)).count();
                var totalActivity = grades.stream().filter(r->r.Type.equals(GradeType.ACTIVITY)).map(r->r.Score).reduce(0.00,Double::sum);
                var countActivity = grades.stream().filter(r->r.Type.equals(GradeType.ACTIVITY)).count();
                var totalLab = grades.stream().filter(r->r.Type.equals(GradeType.LAB)).map(r->r.Score).reduce(0.00,Double::sum);
                var countLab = grades.stream().filter(r->r.Type.equals(GradeType.LAB)).map(r->r.Score).count();
                Double currentScore = Double.parseDouble(activityTextField.getText());

                var newRecord = new GradeRecord();
                newRecord.Score = currentScore;
                var modelRow =  AddNewRecord(newRecord);
                model.addRow(new Object[] { modelRow.IsSelected,modelRow.Type, modelRow.Score,modelRow.GradeId });

                System.out.println("count exam: "+ countExam + ", from addRecord " + totalExam / countExam);
                //set summary
                totalExamLabel.setText(String.format("Total " + EXAMGRADE + ": %.2f", totalExam /countExam));
                totalLabLabel.setText(String.format("Total " + LABGRADE + ": %.2f", totalLab / countLab));
                totalActivityLabel.setText("Total " + ACTIVITYGRADE + ":" + totalActivity / countActivity);
            }
        });

        activityComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var itemSelected = activityComboBox.getSelectedItem();
                CurrentSelectedActivity = itemSelected.toString();
                CurrentSelectAct = GradeType.valueOf(itemSelected.toString());;
                System.out.println(CurrentSelectedActivity);
            }
        });
    }


    public GradeRecord AddNewRecord(GradeRecord record){

        if(record.Score > 100 || record.Score <= 50){
            new Throwable("Invalid Score must be range of 50 to 100");
        }
        //identity of record id
        TOTALRECORDS += 1;
        record.GradeId = TOTALRECORDS;
        record.Type = CurrentSelectAct;
        grades.add(record);
        return record;
    }
    public void ComputeFinalGrade(){

        var summary = new GradeSummary();
        summary.Compute();

        System.out.println("total lab: " + (TotalLab/ summary.CountLab) * ACTIVITYANDLAB_PERCENTAGE);
        System.out.println("total exam: " + ((TotalExam / summary.CountExam) * EXAM_PERCENTAGE));
        System.out.println("total activity: " + ((TotalActivity / summary.CountActivity) * ACTIVITYANDLAB_PERCENTAGE));
        double finalGrade = ((summary.TotalLab / summary.CountLab) * ACTIVITYANDLAB_PERCENTAGE) +
                ((summary.TotalActivity /summary.CountActivity) * ACTIVITYANDLAB_PERCENTAGE) +
                ((summary.TotalExam / summary.CountExam) * EXAM_PERCENTAGE);

        finalGradeLabel.setText(String.format(FINALEGRADE + ": %.2f", finalGrade));
        if (finalGrade < 70) {
            finalGradeLabel.setForeground(Color.RED);
        } else {
            finalGradeLabel.setForeground(Color.BLACK);
        }
    }

    public class GradeRecord {
        public Boolean IsSelected = false;
        public Integer GradeId;
        public Double Score;
        public GradeType Type;
    }

    public  class GradeSummary {

        public double TotalExam;
        public long CountExam;
        public double AverageOfExam;
        public double TotalActivity;
        public long CountActivity;
        public double TotalLab;
        public long CountLab;

        void Compute(){
            TotalExam = grades.stream().filter(r->r.Type.equals(GradeType.EXAM)).map(r->r.Score).reduce(0.00,Double::sum);
            CountExam = grades.stream().filter(r->r.Type.equals(GradeType.EXAM)).count();
            TotalActivity = grades.stream().filter(r->r.Type.equals(GradeType.ACTIVITY)).map(r->r.Score).reduce(0.00,Double::sum);
            CountActivity = grades.stream().filter(r->r.Type.equals(GradeType.ACTIVITY)).count();
            TotalLab = grades.stream().filter(r->r.Type.equals(GradeType.LAB)).map(r->r.Score).reduce(0.00,Double::sum);
            CountLab = grades.stream().filter(r->r.Type.equals(GradeType.LAB)).map(r->r.Score).count();
            AverageOfExam = TotalExam / CountExam;
        }
    }


    enum GradeType {
        ACTIVITY,
        EXAM,
        LAB
    }



}
