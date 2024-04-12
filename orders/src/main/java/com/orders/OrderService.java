package com.orders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

@Component
public class OrderService {

    public Order checkout(Order order) throws Exception {

        // validations
        int discountPercent;
        try {
            discountPercent = Integer.parseInt(order.getDiscountPercent());
        }
        catch (Exception e) {
            throw new Exception("Not a valid number for the field discountPercent");
        }

        if(discountPercent > 100) {
            throw new Exception("Discount percent is not in the range 0-100.");
        }

        if(order.getRentalDays() < 1) {
            throw new Exception("Rental day count is not 1 or greater.");
        }


        getToolDetails_And_calc_charges(order);
        return order;
    }


    /*
       Makes a call to InventoryApplication endpoint and retrieve's tool metadata based on the code.
     */
    private void getToolDetails_And_calc_charges(Order order) throws Exception {

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8085/inventory/code?code="+order.getToolCode()))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = mapper.readTree(response.body());

        String toolType = jsonObj.get("type").asText();
        order.setToolType(toolType);

        double rentalPricePerDay = jsonObj.get("price").asDouble();
        String weekDayCharge = jsonObj.get("weekDayCharge").asText();
        String weekendCharge = jsonObj.get("weekendCharge").asText();
        String holidayCharge = jsonObj.get("holidayCharge").asText();

        int rentalDays = order.getRentalDays();
        LocalDate checkoutDate = order.getCheckoutDate();
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);

        order.setDueDate(dueDate);

        List<LocalDate> dates = checkoutDate.datesUntil(dueDate).toList();

        int daysToCharge = 0;

      for(LocalDate date: dates) {
          boolean isWeekend = FederalHoliday.isWeekend(date);
          boolean isHoliday = FederalHoliday.isObserved(date);

          if(isWeekend && weekendCharge.equalsIgnoreCase("yes")) {
              daysToCharge++;
          }
          else if(!isWeekend && weekDayCharge.equalsIgnoreCase("yes")) {

              if( !isHoliday)
                daysToCharge++;

              else if (holidayCharge.equalsIgnoreCase("yes"))
                  daysToCharge++;
          }
      }

      chargeForDays(order, rentalPricePerDay, daysToCharge);

      daysToCharge = 0 ; //reset the value to '0' .

    }

    private void chargeForDays(Order order, double rentalPricePerDay,  int chargeableDays) {


            int discountPercent = Integer.parseInt(order.getDiscountPercent());
            double preDiscountCharge = rentalPricePerDay * chargeableDays;
            double discountedAmount = preDiscountCharge * discountPercent / 100;
            double afterDiscountCharge = preDiscountCharge - discountedAmount;

            BigDecimal bdDiscountedAmount = new BigDecimal(Double.toString(discountedAmount));
                 bdDiscountedAmount = bdDiscountedAmount.setScale(2, RoundingMode.HALF_UP);

            BigDecimal finalCharge = new BigDecimal(Double.toString(afterDiscountCharge));
                 finalCharge = finalCharge.setScale(2, RoundingMode.HALF_UP);

            order.setChargeDays(chargeableDays);
            order.setDailyRentalCharge(rentalPricePerDay);
            order.setPreDiscountCharge("$"+preDiscountCharge);
            order.setDiscountedAmount("$"+bdDiscountedAmount.doubleValue());
            order.setFinalCharge("$"+finalCharge.doubleValue());
            order.setDiscountPercent(order.getDiscountPercent()+"%");
    }
}


