import java.util.ArrayList;

public  class GradeSummary {
    private  ArrayList<GradeRecord> _grades = new ArrayList<>();
    public  GradeSummary(ArrayList<GradeRecord> grades){
       _grades = grades;
    }
    public double TotalExam;
    public long CountExam;
    public double AverageOfExam;
    public double TotalActivity;
    public long CountActivity;
    public double TotalLab;
    public long CountLab;
    double EXAM_PERCENTAGE = 0.40;
    double ACTIVITYANDLAB_PERCENTAGE = 0.30;
    double AverageOfActivity =0.00;
    double AverageOfLab = 0.00;
    void Compute(){
        TotalExam = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.EXAM)).map(r->r.Score).reduce(0.00,Double::sum);
        CountExam = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.EXAM)).count();
        TotalActivity = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.ACTIVITY)).map(r->r.Score).reduce(0.00,Double::sum);
        CountActivity = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.ACTIVITY)).count();
        TotalLab = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.LAB)).map(r->r.Score).reduce(0.00,Double::sum);
        CountLab = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.LAB)).map(r->r.Score).count();

        AverageOfExam = TotalExam > 0 ? TotalExam / CountExam : 0;
        AverageOfActivity = TotalActivity > 0 ? TotalActivity / CountActivity  : 0;
        AverageOfLab = TotalLab > 0 ? TotalLab / CountLab : 0;
    }
    void Print(){
        System.out.println("total lab: " + AverageOfLab * ACTIVITYANDLAB_PERCENTAGE);
        System.out.println("total exam: " + AverageOfExam * EXAM_PERCENTAGE);
        System.out.println("total activity: " + AverageOfActivity * ACTIVITYANDLAB_PERCENTAGE);
    }
}