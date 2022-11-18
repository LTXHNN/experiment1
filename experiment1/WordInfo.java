package experiment1;

/**
 * @author 李天翔
 * @date 2022/05/11
 **/
public class WordInfo {
    private int row;
    private int col;
    private String word;
    private String type;
    private int diff;

    public WordInfo(int row, int col, String word, String type, int diff) {
        this.row = row;
        this.col = col;
        this.word = word;
        this.type = type;
        this.diff = diff;
    }
    WordInfo(int row, int col, String word, String type) {
        this.row = row;
        this.col = col;
        this.word = word;
        this.type = type;
        this.diff = diff;
    }
    @Override
    public String toString() {
        if(type.equals("Error")){
            return word+"\t\t\t\t"+type+"\t\t\t\t"+type+"\t\t\t\t("+row+","+col+")";
        }else {
            return word+"\t\t\t\t"+"("+diff+","+word+")"+"\t\t\t\t"+type+"\t\t\t\t("+row+","+col+")";
        }
    }
}
