package com.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController  {

@Autowired
InventoryService inventoryService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getToolsList() {

        try {
            return ResponseEntity.ok(inventoryService.getToolList());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body("There are no tools available in our inventory.");
        }
    }

    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public ResponseEntity<?> getToolByCode(@RequestParam String code) {
        try {
            return ResponseEntity.ok(inventoryService.getToolByCode(code));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body("Tool with the code " +code+ " doesn't exists in our inventory.");
        }
    }

    @RequestMapping(value = "/type", method = RequestMethod.GET)
    public ResponseEntity<?> getToolByType(@RequestParam String type) {

        try {
           return ResponseEntity.ok(inventoryService.getToolByType(type));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body("Tool with the type " +type+" doesn't exists in our inventory.");
        }
    }

    @RequestMapping(value = "/code", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestParam String code, @RequestParam int rentCount) {

        try {
            int currentCount = inventoryService.updateToolCount(code,  rentCount);
            return ResponseEntity.ok("Tool count for " +code+ " is successfully updated. " +
                    "Current tool count for " +code+ " is " +currentCount);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body("Tool with the code " +code+" doesn't exists in our inventory.");
        }
    }

}
