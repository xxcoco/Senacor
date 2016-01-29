package com.senacor.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Tabelle 
{
    public static void main( String[] args )
    {
     	try {
    		
     		if(args.length != 2){
    			throw new IllegalArgumentException("Brauche genau zwei Dateien!");
    		}
    		
     		
    		 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
     		 String path_excel = args[0];
    	     
    	     if(!existiert_File(path_excel)) {
    	    	 return;
    	     }
    	     
    	     String path_stunden = args[1];
    	     if(!existiert_File(path_stunden)) {
    	    	 return;
    	     }
    	  
    	     System.out.println("Geben Sie Ihren Namen ein:");
    	     String textName = in.readLine();
    	     
    	     boolean bool = true;
    	     String textKwoche = null;
    	     while (bool) {
	    	     System.out.println("Geben Sie die Kalenderwoche ein:");
	    	     textKwoche = in.readLine();
	    	     if (pruefe_Kwoche(textKwoche)) {
	    	    	bool = false;  
	    	     }     
    	     }
    	     
    	    bool = true;
    	    String textMonat = null;
    	     while(bool) {
    	    	 System.out.println("Geben Sie den Monat an:");
        	     textMonat = in.readLine();
        	     String monat = getMonat(textMonat);
        	     if(monat != null){
        	    	 bool = false;
        	     }
    	     }
    	    
    	     
    	     System.out.println("Möchten Sie die Stunden ausgezahlt bekommen? (J/N");
    	     String auszahlung = in.readLine();
    	     auszahlung = getKreuz(auszahlung);
    	     if(auszahlung == null) {
    	    	 return;
    	     }
    	     
    	     System.out.println("Sollen die angegebenen Stunden mit Ihrem Arbeitszeitkonto verrechent werden?(J/N)");
    	     String gutschrift = in.readLine();
    	     gutschrift = getKreuz(gutschrift);
    	     if(gutschrift == null) {
    	    	 return;
    	     }   	     
    		
    	     String ff = "A:\\02_Vorlage_Stundennachweis_Werkstudenten.xlsx";
    	     String fd = "A:\\nachweis.txt";
    	     
    	    FileInputStream file = new FileInputStream(new File(path_excel));
    	     
    	    XSSFWorkbook workbook = new XSSFWorkbook(file);
    	    XSSFSheet sheet = workbook.getSheetAt(0);
    	        	    
    	    Cell cell_name = null;
    	    Cell cell_woche = null;
    	    Cell cell_monat = null;
    	    cell_name = sheet.getRow(6).getCell(3); // name
    	    cell_woche = sheet.getRow(8).getCell(3); // woche
    	    cell_monat = sheet.getRow(10).getCell(3); // monat
    	    cell_name.setCellValue(textName);
    	    cell_woche.setCellValue(textKwoche);
    	    cell_monat.setCellValue(textMonat);
    	    
    		Files.readAllLines(Paths.get(path_stunden)).stream().forEach(line -> {
    			
    			String[] split = line.split(",");
	    	    if(split.length != 6){
	    	    	System.err.println("Zeile muss aus sechs Argumenten bestehen");
	    	    	return;
	    	    }
    	    	    
	    	    Cell cell_datum = null;
	    	    Cell cell_start = null;
	    	    Cell cell_ende = null;
	    	    Cell cell_pause = null;
	    	    Cell cell_tat = null;
	    	    
	    	    int row = get_Zeile_Wochentag(split[0].trim());
	    	    if(row == 0){
	    	    	System.err.println("1. Angabe ist Wochentag (zB MO)");
	    	    	return;
	    	    }

	    	    try {
					pruefe_Datum(split[1].trim());
					pruefe_Uhrzeit(split[2].trim());
					pruefe_Uhrzeit(split[3].trim());
					pruefe_Uhrzeit(split[4].trim());
				} catch (Exception e1) {
					System.err.println(e1);
					return;
				}
	    
	    	    if(!pruefe_Taetigkeit(split[5])) {
	    	    	return;
	    	    }
	    	    
	           
	    	    cell_datum = sheet.getRow(row).getCell(2);
		    	cell_start = sheet.getRow(row).getCell(3);
		    	cell_ende = sheet.getRow(row).getCell(4);
		    	cell_pause = sheet.getRow(row).getCell(5);
		    	cell_tat = sheet.getRow(row).getCell(7);
		   
		    	cell_datum.setCellValue(split[1].trim());
		    	cell_start.setCellValue(split[2].trim());
		    	cell_ende.setCellValue(split[3].trim());
		    	cell_pause.setCellValue(split[4].trim());
		    	cell_tat.setCellValue(split[5]);
    		});
    	    
    	   
    	    
    	   
    	    file.close();
    	    FileOutputStream out = 
    	        new FileOutputStream(new File("A:\\test.xlsx"));
    	    workbook.write(out);
    	    out.close();
    	     
    	} catch (FileNotFoundException e) {
    	    e.printStackTrace();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }
    
 /*   public static String getInput() throws IOException{
    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    return in.readLine();
    }  */
    
    public static boolean pruefe_Kwoche (String kwoche) {
        boolean bool =true;
        if (Integer.parseInt(kwoche) <= 53 && Integer.parseInt(kwoche) >= 1) {
         bool = true;
        }  
        else { System.out.println("Ungültige Angabe der Kalenderwoche.");  
         bool = false;
         }
        return bool;      
      }
    
    public static String getMonat(String monat){
    	String result;
    
    	switch(Integer.parseInt(monat)){
    		case 1 : result = "Januar"; break;
    		case 2 : result = "Februar"; break;
    		case 3 : result = "März"; break;
    		case 4 : result = "April"; break;
    		case 5 : result = "Mai"; break;
    		case 6 : result = "Juni"; break;
    		case 7 : result = "Juli"; break;
    		case 8 : result = "August"; break;
    		case 9 : result = "September"; break;
    		case 10 : result = "Oktober"; break;
    		case 11 : result = "November"; break;
    		case 12 : result = "Dezember"; break;
    		default : result = null;
    	}
    	return result;
    }

	public static boolean existiert_File(String path) {
    	if(!Files.exists(Paths.get(path))){
			System.err.println(String.format("%s gibt es nicht", path));
			return false;
    	}
		return true;
	}
	
	public static String getKreuz(String in) {
		String kreuz;
		switch(in.toUpperCase()) {
			case "J" : kreuz = "X"; break;
			case "N" : kreuz = ""; break;
			default : kreuz = null; System.out.println("Ungültige Eingabe");
		}
		return kreuz;
	}

	public static boolean pruefe_Taetigkeit(String tat) {
    	if(tat.length() > 50) {
	    	System.err.println("Tätigkeitsbeschreibung ist zu lang");
	    	return false;
	    }
    	else if (tat.length() == 0) {
    		System.err.println("Tätigkeitsbeschreibung fehlt");
	    	return false;
    	}
		return true;
	}

	public static int get_Zeile_Wochentag(String tag) {
    	int row;
    	switch(tag.toUpperCase()){
	    	case "MO": row = 14; break;
	    	case "DI": row = 15; break;
	    	case "MI": row = 16; break;
	    	case "DO": row = 17; break;
	    	case "FR": row = 18; break;
	    	case "SA": row = 19; break;
	    	case "SO": row = 20; break;
	    	default : row = 0;
	    }
    	return row;
    }
    
    public static void pruefe_Datum(String datum) throws ParseException {
    	try {
        	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    	    Date d = sdf.parse(datum);
    	    sdf.setLenient(false);
            
        } catch (ParseException e) {
     //       System.err.println(String.format("Datum %s nicht gültig",datum));
        	throw new ParseException(String.format("Datum %s nicht gültig",datum), 0);
        }
    }
    
    public static void pruefe_Uhrzeit(String zeit) throws ParseException {
    	try {
    	    SimpleDateFormat hf = new SimpleDateFormat("HH:mm");
    	    hf.setLenient(false);
    	    Date h = hf.parse(zeit);
            
        } catch (ParseException e) {
       //     System.err.println(String.format("Uhrzeit %s nicht gültig",zeit));
            throw new ParseException(String.format("Uhrzeit %s nicht gültig",zeit), 0);
        }
    }
}
