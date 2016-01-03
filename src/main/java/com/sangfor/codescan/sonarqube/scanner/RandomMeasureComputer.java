package com.sangfor.codescan.sonarqube.scanner;

import com.sangfor.codescan.sonarqube.CodeScanMetrics;
import org.apache.commons.lang.math.RandomUtils;
import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

public class RandomMeasureComputer implements MeasureComputer {

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext defContext) {
        return defContext.newDefinitionBuilder()
                .setOutputMetrics(CodeScanMetrics.RANDOM.getKey())
                .build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
    // This method is executed on the whole tree of components.
        // Bottom-up traversal : files -> directories -> modules -> project

        double value;
        if (context.getComponent().getType() == Component.Type.FILE) {
            // set a random value on files
            value = RandomUtils.nextDouble();
        } else {
            // directory, module or project: sum values of children
            value = 0.0;
            for (Measure childMeasure : context.getChildrenMeasures(CodeScanMetrics.RANDOM.getKey())) {
                value += childMeasure.getDoubleValue();
            }
        }
        context.addMeasure(CodeScanMetrics.RANDOM.getKey(), value);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
