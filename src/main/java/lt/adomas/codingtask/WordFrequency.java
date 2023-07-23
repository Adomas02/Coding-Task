package lt.adomas.codingtask;

import java.util.Map;

public class WordFrequency {
    private String word;
    private Long frequency;

    public WordFrequency(Map.Entry<String, Long> entry) {
        word = entry.getKey();
        frequency = entry.getValue();
    }

    public WordFrequency(String word, Long frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getFrequency() {
        return frequency;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    public String toLine() {
        return word + " : " + frequency +"\n";
    }

    public String fileName() {
        String partOfFileName ;
        if(word.matches("[A-G].*")){
            partOfFileName= "A-G";
        }else if(word.matches("[H-N].*")){
            partOfFileName= "H-N";
        }else if(word.matches("[O-U].*")){
            partOfFileName= "O-U";
        }else if(word.matches("[V-Z].*")){
            partOfFileName= "V-Z";
        } else{
            partOfFileName= "Error";
        }
        return "Result_"+partOfFileName+".txt";
    }
}
