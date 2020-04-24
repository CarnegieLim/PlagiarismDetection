package com.example.CS575;

import com.example.CS575.util.Line;
import com.example.CS575.util.Preprocessing;
import com.example.CS575.util.Result;
import com.example.CS575.util.TextComparator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ApiController {
    public static class Body {
        public String textA;
        public String textB;
        public String method;
        public Double threshold;

        public Body(String textA, String textB, String method, Double threshold) {
            this.textA = textA;
            this.textB = textB;
            this.method = method;
            this.threshold = threshold;
        }
    }

    @CrossOrigin
    @PostMapping("/compare")
    public Result post(@RequestBody Body body) {
        ArrayList<Line> textA = Preprocessing.preprocess(body.textA,5, true, true);
        ArrayList<Line> textB = Preprocessing.preprocess(body.textB,5, true, true);
        Result res = TextComparator.compare(textA, textB, body.method, body.threshold);
        return res;
    }
}
