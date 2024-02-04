
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class GradingPage extends JFrame {

    int TOTALRECORDS = 0;
    double EXAM_PERCENTAGE = 0.40;
    double ACTIVITYANDLAB_PERCENTAGE = 0.30;
    JLabel totalActivityLabel = new JLabel();
    JLabel totalLabLabel = new JLabel();
    JLabel totalExamLabel = new JLabel();
    JLabel finalGradeLabel = new JLabel();
    JLabel errorMessage = new JLabel();
    String CurrentSelectedActivity = GradeType.ACTIVITY.toString();
    GradeType CurrentSelectAct = GradeType.ACTIVITY;
    String ERROR_MESSAGE_INVALID_RANGE = "\"Invalid Score must be range of 50 to 100\"";
    JComboBox activityComboBox = new JComboBox();
    JTextField activityTextField = new JTextField();
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
    String CurrentUser;

    //load image background here.
    BufferedImage backgroundImage = ImageIO.read(new File("resource/background.jpg"));
    ImagePanel mainPanel = new ImagePanel(backgroundImage);

    GradingPage(String UserName) throws IOException {
        CurrentUser = UserName;
        InitializedComponents();
        SetFontColor();
        SetEvents();
    }
    public void InitializedComponents() {
        this.setTitle("Grading System");
        this.setSize(500,500);
        this.setLayout(new GridLayout(1, 3, 10, 10));
        JLabel userLabel = new JLabel();
        userLabel.setText("Welcome: " + CurrentUser);

        mainPanel.add(userLabel);
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

        AddSubModules();

        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(mainPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }
    private void SetFontColor(){
        //https://codepen.io/HunorMarton/details/eWvewo
        Color color = Color.black;
        totalLabLabel.setForeground(color);
        totalExamLabel.setForeground(color);
        finalGradeLabel.setForeground(color);
        totalActivityLabel.setForeground(color);
    }
    private  void AddSubModules(){

        mainPanel.add(activityTextField);
        mainPanel.add(addRecordBtn);
        mainPanel.add(errorMessage);
        mainPanel.add(totalActivityLabel);
        mainPanel.add(totalLabLabel);
        mainPanel.add(totalExamLabel);
        mainPanel.add(finalGradeLabel);
        mainPanel.add(new JScrollPane(activityTable));
        mainPanel.add(removedRecordsBtn);
        mainPanel.add(finalGradeBtn);
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
                try {
                    AddAndDisplayRecord();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
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

    public class ImagePanel extends JPanel
    {
        private static final long serialVersionUID = 1L;
        private Image image = null;
        private int iWidth2;
        private int iHeight2;

        public ImagePanel(Image image)
        {
            this.image = image;
            this.iWidth2 = image.getWidth(this)/2;
            this.iHeight2 = image.getHeight(this)/2;
        }


        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if (image != null)
            {
                int x = this.getParent().getWidth()/2 - iWidth2;
                int y = this.getParent().getHeight()/2 - iHeight2;
                g.drawImage(image,x,y,this);
            }
        }
    }
    public void AddAndDisplayRecord() throws Exception {
        var summary = new GradeSummary(grades);
        summary.Compute();
        summary.Print();

        Double currentScore = Double.parseDouble(activityTextField.getText());

        var newRecord = new GradeRecord();
        newRecord.Score = currentScore;
        var modelRow =  AddNewRecord(newRecord);
        model.addRow(new Object[] { modelRow.IsSelected,modelRow.Type, modelRow.Score,modelRow.GradeId });
        //set summary
        totalExamLabel.setText(String.format("Total " + EXAMGRADE + ": %.2f", summary.AverageOfExam));
        totalLabLabel.setText(String.format("Total " + LABGRADE + ": %.2f", summary.TotalLab / summary.CountLab));
        totalActivityLabel.setText("Total " + ACTIVITYGRADE + ":" + summary.TotalActivity / summary.CountActivity);
    }
    public GradeRecord AddNewRecord(GradeRecord record) throws Exception {

        if (record.Score <= 50.00 || record.Score > 100){
            errorMessage.setText(ERROR_MESSAGE_INVALID_RANGE);
            errorMessage.setForeground(Color.RED);
            throw new Exception(ERROR_MESSAGE_INVALID_RANGE);
        }

        errorMessage.setText("");
        //identity of record id
        TOTALRECORDS += 1;
        record.GradeId = TOTALRECORDS;
        record.Type = CurrentSelectAct;
        grades.add(record);
        return record;
    }
    public void ComputeFinalGrade(){

        var summary = new GradeSummary(grades);
        summary.Compute();
        summary.Print();

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
    enum GradeType {
        ACTIVITY,
        EXAM,
        LAB
    }

}
