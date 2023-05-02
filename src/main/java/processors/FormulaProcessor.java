package processors;

import dataobjects.CellObject;
import dataobjects.SheetObject;
import dataobjects.ShortCommand;

import java.util.ArrayList;
import java.util.List;

public class FormulaProcessor {
    private SheetObject sheet;
    private CellObject errorCell;

    public FormulaProcessor(SheetObject sheet) {
        this.sheet = sheet;
    }

    String[] commands = {"SUM", "MULTIPLY", "DIVIDE", "GT", "EQ", "NOT", "AND", "OR", "IF", "CONCAT",};

    public CellObject formulaAnalyzer(String statement) {
        List<String> nodes = new ArrayList<>();

        while (nodes.size() != 1) {
            for (String command : commands) {
                if (statement.toUpperCase().contains(command + "(")) {
                    nodes.add(command);
                }
            }
            if (nodes.size() > 1) {
                statement = shortestCommand(statement);
                if (statement.equals("error")) {
                    return errorCell();
                }
                nodes.clear();
            }
        }
        return formulaRouter(statement);
    }

    private String shortestCommand(String statement) {
        ShortCommand shortCommand = new ShortCommand();
        shortCommand.setLength(statement.length());
        for (String command : commands) {
            if (statement.toUpperCase().contains(command)) {
                int start = statement.indexOf(command);
                int end = statement.indexOf(")", start);
                int length = end - start;
                if (shortCommand.getLength() > length) {
                    shortCommand.setStart(start);
                    shortCommand.setEnd(end);
                    shortCommand.setLength(length);
                    shortCommand.setName(command);
                }
            }
        }

        StringBuilder updatedStatement = new StringBuilder();
        CellObject result = formulaRouter(statement.substring(shortCommand.getStart(), shortCommand.getEnd()+ 1));
        if (!result.getType().equals("error")) {
            updatedStatement.append(statement, 0, shortCommand.getStart());
            updatedStatement.append(result.getValue());
            updatedStatement.append(statement, shortCommand.getEnd() + 1, statement.length());
        } else {
            updatedStatement.append("error");
        }
        return updatedStatement.toString();
    }

    public CellObject formulaRouter(String statement) {
        //let's assume SUM elements will be values or references only
        if (statement.toUpperCase().contains("SUM(")) {
            return processSum(extractElements(statement, "SUM"));
        }
        if (statement.toUpperCase().contains("MULTIPLY(")) {
            return processMultiply(extractElements(statement, "MULTIPLY"));
        }
        if (statement.toUpperCase().contains("DIVIDE(")) {
            return processDivide(extractElements(statement, "DIVIDE"));
        }
        if (statement.toUpperCase().contains("GT(")) {
            return processGt(extractElements(statement, "GT"));
        }
        if (statement.toUpperCase().contains("EQ(")) {
            return processEq(extractElements(statement, "EQ"));
        }
        if (statement.toUpperCase().contains("NOT(")) {
            return processNot(extractElements(statement, "NOT"));
        }
        if (statement.toUpperCase().contains("AND(")) {
            return processAnd(extractElements(statement, "AND"));
        }
        if (statement.toUpperCase().contains("OR(")) {
            return processOr(extractElements(statement, "OR"));
        }
        if (statement.toUpperCase().contains("IF(")) {
            return processIf(extractElements(statement, "IF"));
        }
        if (statement.toUpperCase().contains("CONCAT(")) {
            return processConcat(extractElements(statement, "CONCAT"));
        }

        return errorCell();
    }

    private CellObject processConcat(String[] elements) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String element : elements) {
            if(element.charAt(0) == ' '){
                element = element.substring(1);
            }
            element = element.replace("\"","");

            if (element.matches("^[a-zA-Z]\\d$") && sheet.checkCellByIndex(element.toUpperCase())) {
                CellObject referredCell = finalValue(element.trim());
                if (!referredCell.getType().equals("string")) {
                    return errorCell();
                }
                stringBuilder.append(referredCell.getValue());
            } else {
                stringBuilder.append(element);
            }
        }
        return TypeProcessor.fillCellValues(new CellObject(stringBuilder.toString()));
    }

    private CellObject processIf(String[] elements) {
        if (elements.length != 3) {
            return errorCell();
        }
        CellObject newCell = new CellObject(elements[0]);
        TypeProcessor.fillCellValues(newCell);
        if (newCell.getType().equals("boolean")) {
            if (newCell.getBooleanValue()) {
                return finalValue(elements[1]);
            } else {
                return finalValue(elements[2]);
            }
        }
        return errorCell();
    }

    private CellObject processOr(String[] elements) {
        List<Boolean> values = elementsToBooleans(elements);
        if (values.isEmpty() || elements.length != values.size()) {
            return errorCell();
        }
        for (boolean value : values) {
            if (value) {
                return toBooleanCell(true);
            }
        }
        return toBooleanCell(false);
    }

    private CellObject processAnd(String[] elements) {
        List<Boolean> values = elementsToBooleans(elements);
        if (values.isEmpty() || elements.length != values.size()) {
            return errorCell();
        }
        for (boolean value : values) {
            if (!value) {
                return toBooleanCell(false);
            }
        }
        return toBooleanCell(true);
    }

    private CellObject processNot(String[] elements) {
        List<Boolean> values = elementsToBooleans(elements);
        if (values.size() != 1) {
            return errorCell();
        }
        return toBooleanCell(!values.get(0));
    }

    private CellObject processEq(String[] elements) {
        List<Double> numbers = elementsToNumbers(elements);
        if (numbers.size() != 2) {
            return errorCell();
        }
        return toBooleanCell(numbers.get(0) - numbers.get(1) == 0);
    }

    private CellObject processGt(String[] elements) {
        List<Double> numbers = elementsToNumbers(elements);
        if (numbers.size() != 2) {
            return errorCell();
        }
        return toBooleanCell(numbers.get(0) > numbers.get(1));
    }

    private static CellObject toBooleanCell(Boolean value) {
        CellObject resultCell = new CellObject(value.toString());
        resultCell.setProcessed(true);
        resultCell.setType("boolean");
        resultCell.setBooleanValue(value);
        return resultCell;
    }

    private CellObject processDivide(String[] elements) {
        List<Double> numbers = elementsToNumbers(elements);
        if (numbers.isEmpty()) {
            return errorCell();
        }
        double result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) != 0) {
                result = result / numbers.get(i);
            } else {
                return errorCell();
            }
        }
        return toNumberCell(result);
    }

    private CellObject processMultiply(String[] elements) {
        double result = 1.0;
        List<Double> numbers = elementsToNumbers(elements);
        if (numbers.isEmpty()) {
            return errorCell();
        }
        for (double number : numbers) {
            result = result * number;
        }
        return toNumberCell(result);
    }

    public CellObject processSum(String[] elements) {

        double result = 0.0;
        List<Double> numbers = elementsToNumbers(elements);
        if (numbers.isEmpty()) {
            return errorCell();
        }
        for (double number : numbers) {
            result = result + number;
        }
        return toNumberCell(result);
    }

    private static CellObject toNumberCell(double result) {
        CellObject resultCell = new CellObject("0");
        resultCell.setProcessed(true);
        if (result % 1 == 0) {
            resultCell.setType("integer");
            resultCell.setIntValue((int) result);
            resultCell.setValue(String.valueOf(resultCell.getIntValue()));
        } else {
            resultCell.setType("float");
            resultCell.setFloatValue(result);
            resultCell.setValue(String.valueOf(resultCell.getFloatValue()));
        }
        return resultCell;
    }

    public List<Boolean> elementsToBooleans(String[] elements) {
        List<Boolean> values = new ArrayList<>();
        for (String element : elements) {
            element = element.trim();

            if (element.equalsIgnoreCase("TRUE") || element.equalsIgnoreCase("FALSE")) {
                values.add(Boolean.parseBoolean(element));
            } else if (element.matches("^[a-zA-Z]\\d$") && sheet.checkCellByIndex(element.toUpperCase())) {
                CellObject referredCell = finalValue(element);
                if (referredCell.getType().equals("boolean")) {
                    values.add(referredCell.getBooleanValue());
                }

            } else {
                return new ArrayList<>();
            }
        }
        return values;
    }

    public List<Double> elementsToNumbers(String[] elements) {
        List<Double> numbers = new ArrayList<>();

        for (String element : elements) {
            element = element.trim();
            if (TypeProcessor.isNumber(element)) {
                numbers.add(Double.parseDouble(element));
            } else if (element.matches("^[a-zA-Z]\\d$") && sheet.checkCellByIndex(element.toUpperCase())) {
                CellObject referredCell = finalValue(element);
                if (referredCell.getType().equals("integer") || referredCell.getType().equals("float")) {
                    numbers.add(Double.parseDouble(referredCell.getValue()));
                } else {
                    return new ArrayList<>() {
                    };
                }
            } else {
                return new ArrayList<>();
            }
        }
        return numbers;
    }

    public CellObject finalValue(String reference) {
        CellObject cell = sheet.getCellByIndex(reference.trim());

        while (!cell.isProcessed()) {
            cell = sheet.getCellByIndex(cell.getReferenceValue());
        }
        return cell;
    }

    private static String[] extractElements(String statement, String operator) {
        String elements =statement.substring(operator.length() + 1, statement.length() - 1);
        String spliterator = ",";
        if(elements.contains("\", \", \", \"")){
            spliterator = String.valueOf((char) 170);
            elements = elements.replaceAll("\", \", \", \"", "\""+spliterator+" \", \""+spliterator+" \"");
            elements = elements.replaceAll("\"","");
        }
       return elements.split(String.valueOf(spliterator));
    }

    private CellObject errorCell() {
        errorCell = new CellObject("#ERROR: TBD");
        errorCell.setProcessed(true);
        errorCell.setType("error");
        return errorCell;
    }

}
