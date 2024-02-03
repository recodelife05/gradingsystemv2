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

    void Compute(){
        TotalExam = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.EXAM)).map(r->r.Score).reduce(0.00,Double::sum);
        CountExam = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.EXAM)).count();
        TotalActivity = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.ACTIVITY)).map(r->r.Score).reduce(0.00,Double::sum);
        CountActivity = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.ACTIVITY)).count();
        TotalLab = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.LAB)).map(r->r.Score).reduce(0.00,Double::sum);
        CountLab = _grades.stream().filter(r->r.Type.equals(GradingPage.GradeType.LAB)).map(r->r.Score).count();
        AverageOfExam = TotalExam / CountExam;
    }
}