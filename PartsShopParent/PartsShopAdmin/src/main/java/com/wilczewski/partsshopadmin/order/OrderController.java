package com.wilczewski.partsshopadmin.order;

import com.wilczewski.partsshopadmin.brand.BrandServiceImp;
import com.wilczewski.partsshopadmin.setting.SettingService;
import com.wilczewski.partsshopcommon.entity.Brand;
import com.wilczewski.partsshopcommon.entity.Order;
import com.wilczewski.partsshopcommon.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class OrderController {

    private OrderService orderService;
    private SettingService settingService;

    @Autowired
    public OrderController(OrderService orderService, SettingService settingService) {
        this.orderService = orderService;
        this.settingService = settingService;
    }
    @GetMapping("/orders")
    public String listFirstPage(Model model, HttpServletRequest request){

        return listByPage(1, model, "orderTime", "desc", null, request);
    }

    @GetMapping("/orders/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNumber, Model model, String sortField, String sortDir, String keyword, HttpServletRequest request){
        loadCurrencySetting(request);

        Page<Order> page = orderService.listByPage(pageNumber, sortField, sortDir, keyword);
        List<Order> listOrders = page.getContent();


        long startCount = (pageNumber-1) * OrderServiceImp.ORDERS_PER_PAGE + 1;
        long stopCount = startCount + OrderServiceImp.ORDERS_PER_PAGE - 1;
        if (stopCount > page.getTotalElements()) {
            stopCount = page.getTotalElements();
        }

        String reverseSort = sortDir.equals("asc") ? "desc" : "asc";


        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("stopCount", stopCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSort", reverseSort);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listOrders", listOrders);



        return "orders";

    }

    private void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> currencySettings = settingService.getCurrencySettings();

        for (Setting setting : currencySettings) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request){

        try {
            Order order = orderService.get(id);
            loadCurrencySetting(request);
            model.addAttribute("order", order);

            return "order_details_modal";
        } catch (OrderNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());

            return "redirect:/admin/orders" ;
        }
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){

        try {
            orderService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Zamówienie o ID " + id + " zostało usunięte.");
        } catch (OrderNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }

        return "redirect:/admin/orders";
    }

}
