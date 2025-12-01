package com.example.asigurari.component;

import org.springframework.stereotype.Component;

@Component
public class NotificareComponent {

    public void trimiteNotificare(String mesaj) {
        // In real life ai trimite email / SMS; aici doar afisam in consola
        System.out.println("[NOTIFICARE] " + mesaj);
    }
}
