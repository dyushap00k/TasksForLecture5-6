package com.task1;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

public class Task1 {
    private Task1() {
    }

    private static final FineList FINE_LIST = new FineList();
    private static final ConcurrentHashMap<String, BigDecimal> CONTAINER = new ConcurrentHashMap<>();

    public static void doCalculateJsonToXml(File file) throws JAXBException {
        if (file == null) return;
        if (file.isDirectory()) {
            long start = System.currentTimeMillis();
            ExecutorService threadPool = Executors.newFixedThreadPool(8);
            for (File temp : Objects.requireNonNull(file.listFiles())) {
                CompletableFuture.supplyAsync(() -> temp, threadPool).thenAccept(x -> {
                    calculateAmountFinesByTypeFromJsonToXml(temp);
                    System.out.println(System.currentTimeMillis() - start);
                });
            }
            threadPool.shutdown();
            while (!threadPool.isTerminated()) ;
            createResultXmlFile();
        }
    }

    public static void doCalculateXmlToJson(File file) {
        if (file == null) return;
        long start = System.currentTimeMillis();
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        if (file.isDirectory()) {
            for (File temp : Objects.requireNonNull(file.listFiles())) {
                CompletableFuture.supplyAsync(() -> temp, threadPool).thenAccept(x -> {
                    calculateAmountFinesByTypeFromXmlToJson(temp);
                    System.out.println(System.currentTimeMillis() - start);
                });
            }
            threadPool.shutdown();
            while (!threadPool.isTerminated()) ;
            createResultJsonFile();

        }
    }

    private static void calculateAmountFinesByTypeFromJsonToXml(File file) {
        if (file == null || !file.getAbsolutePath().endsWith(".json"))
            return;
        JsonFactory jsonFactory = new JsonFactory();
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             JsonParser jsonParser = jsonFactory.createParser(reader)) {
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                String field;
                String name = "";
                BigDecimal amount = new BigDecimal(0);
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    if (jsonParser.currentName() != null) {

                        field = jsonParser.currentName().toUpperCase(Locale.ROOT);
                        if (field.equals("TYPE"))
                            name = jsonParser.getValueAsString();
                        if (field.equals("FINE_AMOUNT"))
                            amount = BigDecimal.valueOf(jsonParser.getValueAsDouble());
                    }
                }
                synchronized (CONTAINER) {
                    CONTAINER.put(name, CONTAINER.getOrDefault(name, BigDecimal.ZERO).add(amount));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createResultXmlFile() throws JAXBException {
        List<task3.Fine> list = new ArrayList<>();
        CONTAINER.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .forEach(x -> list.add(new task3.Fine(x.getKey(), x.getValue())));
        FINE_LIST.getList().addAll(list);
        JAXBContext jaxbContext = JAXBContext.newInstance(FineList.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(FINE_LIST, new File("src/main/resources/result.xml"));
    }


    private static void calculateAmountFinesByTypeFromXmlToJson(File file) {
        if (file == null || !file.getAbsolutePath().endsWith(".xml"))
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            JAXBContext jaxbContext = JAXBContext.newInstance(FineList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            FineList list = (FineList) unmarshaller.unmarshal(reader);
            synchronized (CONTAINER) {
                list.getList()
                        .forEach(x -> CONTAINER.put(x.getType(), CONTAINER.getOrDefault(x.getType(), BigDecimal.ZERO).add(x.getAmount())));
            }
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void createResultJsonFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/result.json"))) {
            ObjectMapper oMapper = new ObjectMapper();
            ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
            for (String str : CONTAINER.keySet()) {
                ObjectNode oNode = oMapper.createObjectNode();
                oNode.put(str.toLowerCase(Locale.ROOT), CONTAINER.get(str).doubleValue());
                arrayNode.add(oNode);
            }
            writer.write(arrayNode.toString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
