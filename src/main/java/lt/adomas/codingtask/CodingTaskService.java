package lt.adomas.codingtask;


import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.lang.Character.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

@Service
public class CodingTaskService {

    private List<WordFrequency> countFrequencies(String newText) {

        Map<String, Long> wordMap = Stream.of(newText.split("[ (),.:\r\n\t]"))
                .map(this::normalizeWord)
                .filter(word -> word.matches(".*[A-Z].*"))
                .collect( groupingByConcurrent( Function.identity(), counting() ));


        return wordMap
                .entrySet()
                .parallelStream()
                .map(WordFrequency::new)
                .toList();
    }

    private String normalizeWord(String word){
        word=word.trim();
        if(!word.isEmpty()){
            if(!isLetter(word.charAt(0))&&!isDigit(word.charAt(0))){
                word = word.substring(1);
            } else if (!isLetter(word.charAt(word.length()-1))&&!isDigit(word.charAt(word.length()-1))) {
                word = word.substring(word.length()-1);
            }
            return word.toUpperCase();
        }
        else {
            return word;
        }

    }


    public byte[] resultToZip(String inputFileContent) {

        ConcurrentMap<String, List<WordFrequency>> fileWordMap = groupingByFiles(inputFileContent);

        var out = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(out)) {
            var writer = new OutputStreamWriter(zipOutputStream);

            for(var fw:fileWordMap.entrySet()){
                zipOutputStream.putNextEntry(new ZipEntry(fw.getKey()));
                zipOutputStream.flush();

                for (var word : fw.getValue()) {
                    writer.write(word.toLine());
                }
                writer.flush();
            }
            zipOutputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

    protected ConcurrentMap<String, List<WordFrequency>> groupingByFiles(String inputFileContent) {

        var wordList = countFrequencies(inputFileContent);

        ConcurrentMap<String, List<WordFrequency>> fileWordMap = wordList
                .parallelStream()
                .collect(groupingByConcurrent(
                        WordFrequency::fileName,
                        collectingAndThen(
                                toList(),
                                list -> list
                                        .parallelStream()
                                        .sorted(comparing(WordFrequency::getWord))
                                        .toList())


                ));

        fileWordMap=new ConcurrentHashMap<>(fileWordMap);// perkuriam mapa kad butu modifikuojamas, kad .put nenuluztu
        fileWordMap.put("output.txt", wordList
                .parallelStream()
                .sorted(comparing(WordFrequency::getWord))
                .toList());
        return fileWordMap;
    }
}
