package processors;

import dataobjects.CellObject;

public class TypeProcessor {
    public static CellObject fillCellValues(CellObject cell) {
        String cellValue = cell.getValue();
        if(cellValue.charAt(0) != '=') {
            //boolean
            if (cellValue.equalsIgnoreCase("true") || cellValue.equalsIgnoreCase("false")) {
                cell.setType("boolean");
                cell.setBooleanValue(booleanValue(cellValue));
                cell.setProcessed(true);
                return cell;
            }
            //number
            if ((cellValue.substring(0, 1).matches("^[0-9]")
                    || cellValue.charAt(0) == '-'
                    || cellValue.charAt(0) == '.')) {

                if (cellValue.charAt(0) == '.') {
                    cellValue = '0' + cellValue;
                }

                if (isNumber(cellValue)) {
                  return toNumber(cell);
                } else {
                    //String
                    cell.setType("string");
                    cell.setProcessed(true);
                    return cell;
                }
            }
        }else {
            //reference
          if (cellValue.matches("^=[a-zA-Z]\\d$")) {
                cell.setType("reference");
                cell.setReferenceValue(cellValue.substring(1));
                cell.setProcessed(false);
                return cell;
            }

            //formula
            if (isFormula(cellValue)) {
                cell.setType("formula");
                cell.setFormulaValue(cellValue.substring(1));
                cell.setProcessed(false);
                return cell;
            }
        }
        cell.setType("string");
        cell.setProcessed(true);
        return cell;
    }

    private static boolean isFormula(String cellValue) {
        String[] commands = {"SUM", "MULTIPLY", "DIVIDE", "EQ","NOT", "AND", "OR", "IF", "CONCAT","GT"};
        for(String command : commands){
            if(cellValue.toUpperCase().contains(command)){
                return true;
            }
        }
        return false;
    }

    private static boolean booleanValue(String value) {
        return value.equalsIgnoreCase("true");
    }

    public static boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String value) {
     try{
         int number = Integer.parseInt(value);
         return true;
     }catch (Exception e){
         return false;
     }
    }

    public static CellObject toNumber(CellObject cell){

        if (isInteger(cell.getValue())) {
            cell.setType("integer");
            cell.setIntValue(Integer.parseInt(cell.getValue()));
        } else {
            cell.setType("float");
            cell.setFloatValue(Double.parseDouble(cell.getValue()));
        }
        cell.setProcessed(true);
        return cell;
    }

}
