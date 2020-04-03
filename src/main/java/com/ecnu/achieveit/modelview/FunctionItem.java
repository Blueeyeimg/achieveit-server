package com.ecnu.achieveit.modelview;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FunctionItem implements Serializable{

    private String primaryFunction;

    private List<String> secondaryFunction = new ArrayList<>();

    private static final long serialVersionUID = 1L;

}
