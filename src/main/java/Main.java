import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class Main {

    private static final String fileName = "t1_katalog.txt";
    private static final String fileNameXML = "xmlText.xml";

    private static boolean tableCreatedBefore = false;

    private static JTable jt = new JTable();

    public static void main(String[] args) throws IOException {



        JFrame f = new JFrame("Integracja Systemów - Maciej Gieroba");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //JPanel panel = new JPanel();

        JButton b2 = new JButton("Zapisz dane do pliku TXT");

        JButton b1 = new JButton("Wczytaj dane z pliku TXT");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File(fileName);
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                String column2Format = "%-25.25s";
                String formatInfo = "%-2.2s" + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format;
                String naglowek = new String("nazwa producenta;przekątna ekranu;rozdzielczość ekranu;rodzaj powierzchni ekranu;czy ekran jest dotykowy;nazwa procesora;liczba rdzeni fizycznych;prędkość taktowania MHz;wielkość pamięci RAM;pojemność dysku;rodzaj dysku;nazwa układu graficznego;pamięć układu graficznego;nazwa systemu operacyjnego;rodzaj napędu fizycznego w komputerze ");
                String[] s = naglowek.split(";");
                //System.out.format(formatInfo, s[0],s[1],s[2],s[3],s[4],s[5],s[6],s[7],s[8],s[9],s[10],s[11],s[12],s[13],s[14]);

                String line;
                int row =1;
                String[] column = s;
                HashMap mapa = new HashMap();

                int k=0;
                while (true) {
                    try {
                        k++;
                        if (!((line = br.readLine()) != null)) break;
                    } catch (IOException ioException) {
                        line = ";;;;;;;;;;;;;;";
                        ioException.printStackTrace();
                    }
                }
                String[][] data = new String[k][16];
                try {
                    br = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                while (true) {
                    try {
                        if (!((line = br.readLine()) != null)) break;
                    } catch (IOException ioException) {
                        line = ";;;;;;;;;;;;;;";
                        ioException.printStackTrace();
                    }
                    s = line.split(";", -1);
                    if(mapa.containsKey(s[0])){
                        mapa.put(s[0], (int)mapa.get(s[0])+1);
                    }
                    else{
                        mapa.put(s[0],1);
                    }
                    for(int i=0; i<s.length; i++){
                        data[row-1][i]=s[i];
                    }
                    row++;
                }
                try {
                    br = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                //---------
                jt = new JTable(data,column);

                jt.setCellSelectionEnabled(true);

                JScrollPane sp=new JScrollPane(jt);
                sp.setSize(1500,500);
                JPanel panel2 = new JPanel();
                panel2.add(sp);
                panel2.setSize(sp.getSize());
                panel2.updateUI();
                if(f.getContentPane().getComponentCount()>1){
                    f.getContentPane().remove(1);
                }
                f.getContentPane().add(panel2, BorderLayout.NORTH);
                b2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println(jt.getRowCount() + " " + jt.getColumnCount());
                        //System.out.println(prepareDataToSave(jt.getRowCount(), jt.getColumnCount(), jt));
                        try (
                                final BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName),
                                StandardCharsets.UTF_8, StandardOpenOption.WRITE);
                        ) {
                            writer.write(prepareDataToSave(jt.getRowCount(), jt.getColumnCount(), jt));
                            writer.flush();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });
            }
        });

        JButton b3 = new JButton("Zapisz dane do XML");
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                Laptops laptops = new Laptops((dtf.format(now)));

                for(int k=0; k< jt.getRowCount(); k++){
                    Laptop laptop = new Laptop((String) jt.getValueAt(k,0),(String) jt.getValueAt(k,8),(String) jt.getValueAt(k,13),
                            (String) jt.getValueAt(k,14), new Screen((String) jt.getValueAt(k,4), (String) jt.getValueAt(k,1),(String) jt.getValueAt(k,2),(String) jt.getValueAt(k,3)),
                            new Processor((String) jt.getValueAt(k,5), (String) jt.getValueAt(k,6), (String) jt.getValueAt(k,7)),
                            new Disc((String) jt.getValueAt(k,10), (String) jt.getValueAt(k,9)),
                            new Graphic_card((String) jt.getValueAt(k,11),(String) jt.getValueAt(k,12)));
                    laptop.id = Integer.toString(k+1);
                    laptops.laptopts.add(laptop);
                }

                jaxbObjectToXML(laptops);
            }
        });

        JButton b4 = new JButton("Wczytaj dane z XML");
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Laptops laptopsToInsertToTable = jaxbXMLToObject();
                    String naglowek = new String("nazwa producenta;przekątna ekranu;rozdzielczość ekranu;rodzaj powierzchni ekranu;czy ekran jest dotykowy;nazwa procesora;liczba rdzeni fizycznych;prędkość taktowania MHz;wielkość pamięci RAM;pojemność dysku;rodzaj dysku;nazwa układu graficznego;pamięć układu graficznego;nazwa systemu operacyjnego;rodzaj napędu fizycznego w komputerze ");
                    String[] s = naglowek.split(";");
                    String[] column = s;

                    String[][] data = new String[laptopsToInsertToTable.laptopts.size()][16];
                    data = formatDataFromXmlToObject(laptopsToInsertToTable, data);
                    jt = new JTable(data, column);

                    jt.setCellSelectionEnabled(true);

                    JScrollPane sp=new JScrollPane(jt);
                    sp.setSize(1500,500);
                    JPanel panel2 = new JPanel();
                    panel2.add(sp);
                    panel2.setSize(sp.getSize());
                    panel2.updateUI();
                    if(f.getContentPane().getComponentCount()>1){
                        f.getContentPane().remove(1);
                    }
                    f.getContentPane().add(panel2, BorderLayout.NORTH);


                } catch (JAXBException jaxbException) {
                    jaxbException.printStackTrace();
                }
            }
        });
        setToolbar(b1, b2, b3, b4, f);


        f.setSize(1600, 900);
        f.setVisible(true);
    }

    private static void jaxbObjectToXML(Laptops laptops)
    {

        try
        {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Laptops.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Print XML String to Console
            StringWriter sw = new StringWriter();


            //Write XML to StringWriter
            jaxbMarshaller.marshal(laptops, new File("xmlText.xml"));

            //Verify XML Content
            String xmlContent = sw.toString();
            System.out.println( xmlContent );

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static Laptops jaxbXMLToObject() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Laptops.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        File xmlFile = new File(fileNameXML);

        Laptops laptops = (Laptops) jaxbUnmarshaller.unmarshal(xmlFile);
        return laptops;
    }

    private static void setToolbar(JButton b1, JButton b2, JButton b3, JButton b4, JFrame f){
        JToolBar toolbar = new JToolBar();
        toolbar.setRollover(true);
        toolbar.add(b1);
        toolbar.addSeparator();
        toolbar.add(b2);
        toolbar.addSeparator();
        toolbar.add(b3);
        toolbar.addSeparator();
        toolbar.add(b4);
        Container contentPane = f.getContentPane();
        contentPane.add(toolbar, BorderLayout.SOUTH);
    }

    private static String prepareDataToSave(int rowCount, int columnCount, JTable table){
        String textToSave = "";
        boolean endOfFile=false;

        for(int k=0; k<rowCount; k++){
            for(int i=0; i<columnCount; i++){
                //System.out.println(table.getValueAt(k,i));
                if(table.getValueAt(k,i) == null){
                    endOfFile=true;
                    break;
                }
                textToSave = textToSave + table.getValueAt(k,i)+";";
            }
            if(!endOfFile){
                textToSave = textToSave +"\n";
            }

        }
        //System.out.println(textToSave);
        return textToSave;
    }

    private static String[][] formatDataFromXmlToObject(Laptops dataFromXml, String[][] data){
        for(int y=0; y<dataFromXml.laptopts.size()-1; y++){
            data[y][0]=dataFromXml.laptopts.get(y).manufacturer;
            data[y][1]=dataFromXml.laptopts.get(y).screen.size;
            data[y][2]=dataFromXml.laptopts.get(y).screen.resolution;
            data[y][3]=dataFromXml.laptopts.get(y).screen.type;
            data[y][4]=dataFromXml.laptopts.get(y).screen.touch;
            data[y][5]=dataFromXml.laptopts.get(y).processor.name;
            data[y][6]=dataFromXml.laptopts.get(y).processor.physical_cores;
            data[y][7]=dataFromXml.laptopts.get(y).processor.clock_speed;
            data[y][8]=dataFromXml.laptopts.get(y).ram;
            data[y][9]=dataFromXml.laptopts.get(y).disc.storage;
            data[y][10]=dataFromXml.laptopts.get(y).disc.type;
            data[y][11]=dataFromXml.laptopts.get(y).graphic_card.name;
            data[y][12]=dataFromXml.laptopts.get(y).graphic_card.memory;
            data[y][13]=dataFromXml.laptopts.get(y).os;
            data[y][14]=dataFromXml.laptopts.get(y).disc_reader;
        }
        return data;
    }
}
