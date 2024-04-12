package com.inventory;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class InventoryService {

Map<String, Integer> inv;

    {
        inv = new HashMap<>();
        inv.put("CHNS", 50);
        inv.put("LADW", 80);
        inv.put("JAKD", 100);
        inv.put("JAKR", 120);
    }



    public List<Tool> getToolList() throws Exception {

        List<Tool> toolList = new ArrayList<>();

        for(Tools t : Tools.values()){

            if(inv.get(t.name()) > 0) {
                Tool tool = new Tool(t.getType(), t.getBrand(),
                        t.name(),
                        Prices.valueOf(t.getType()).getPrice(),
                        Prices.valueOf(t.getType()).getWeekdayCharge(),
                        Prices.valueOf(t.getType()).getWeekendCharge(),
                        Prices.valueOf(t.getType()).getHolidayCharge()
                );

                toolList.add(tool);
            }
        }
        if (toolList.isEmpty()) {
            throw new Exception();
        }
        return toolList;
    }

    public Tool getToolByCode(String code) throws Exception {
        Tool tool = null;

        if(inv.get(code) > 0) {
            Optional<Tools> optionalTools = Arrays.stream(Tools.values())
                    .filter(t -> t.name().equalsIgnoreCase(code))
                    .findFirst();

            if (optionalTools.isPresent()) {
                Tools t = optionalTools.get();
                tool = new Tool(t.getType(), t.getBrand(),
                        t.name(),
                        Prices.valueOf(t.getType()).getPrice(),
                        Prices.valueOf(t.getType()).getWeekdayCharge(),
                        Prices.valueOf(t.getType()).getWeekendCharge(),
                        Prices.valueOf(t.getType()).getHolidayCharge());
            }
        }
        if (Objects.isNull(tool)) {
            throw new Exception();
        }
        return tool;
    }


    public List<Tool> getToolByType(String type) throws Exception {
        Tool tool = null;
        List<Tool> toolByNameList = new ArrayList<>();

        List<Tools> optionalTools = Arrays.stream(Tools.values())
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .toList();

        for(Tools t : optionalTools) {

            if (inv.get(t.name()) > 0) {
                tool = new Tool(t.getType(), t.getBrand(),
                        t.name(),
                        Prices.valueOf(t.getType()).getPrice(),
                        Prices.valueOf(t.getType()).getWeekdayCharge(),
                        Prices.valueOf(t.getType()).getWeekendCharge(),
                        Prices.valueOf(t.getType()).getHolidayCharge());

                toolByNameList.add(tool);
            }

            if (toolByNameList.isEmpty()) {
                throw new Exception();
            }
        }
        return toolByNameList;
    }


    public int updateToolCount(String code, int rentCount) throws Exception {

        if (inv.get(code) > 0) {
            inv.put(code, inv.get(code) - rentCount);
        }
        else {
            throw new Exception();
        }

        return inv.get(code);
    }

}
