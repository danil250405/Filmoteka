//package org.glazweq.demo.controllers;
//
//
//import org.glazweq.demo.domain.Message;
//import org.glazweq.demo.repos.MessageRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.Map;
//
//@Controller
//public class MainController {
//    @Autowired
//    private MessageRepo messageRepo;
//    @GetMapping("/")
//    public String greeting(Map<String, Object> model) {
//
//        return "home";
//    }
//    @GetMapping("/main")
//    public String mainPage(Map<String, Object> model){
//        Iterable<Message> messages = messageRepo.findAll();
//        model.put("messages", messages);
//        return "main";
//    }
//
//    @PostMapping("/add")
//    public String add(@RequestParam String text, @RequestParam String tag, Map<String, Object> model){
//         Message message = new Message(text, tag);
//         messageRepo.save(message);
//         Iterable<Message> messages = messageRepo.findAll();
//         model.put("messages", messages);
//        return "main";
//    }
//    @PostMapping("/filter")
//    public String filter(@RequestParam String filter, Map<String, Object> model){
//        Iterable<Message> messages;
//        if (filter == null || filter.isEmpty()){
//            messages = messageRepo.findAll();
//        }else {
//            messages = messageRepo.findByTag(filter);
//        }
//        //        List<Message> messages = messageRepo.findByTag(filter);
//
//        model.put("messages", messages);
//      return "main";
//    }
//}