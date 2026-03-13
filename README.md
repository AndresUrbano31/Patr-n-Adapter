Sistema de Pasarela de Pagos — Patrones Adapter y Bridge
Planteamiento del Problema
Una aplicación de e-commerce necesita procesar pagos a través de múltiples proveedores externos (Stripe, PayPal, etc.). Cada proveedor tiene su propio SDK con métodos completamente diferentes e incompatibles entre sí.
Además, el sistema debe soportar diferentes tipos de pago (estándar, con descuento, con impuestos) que deben funcionar con cualquier proveedor.
Los Dos Problemas Principales
Problema 1 — Interfaces incompatibles:
Cada proveedor de pagos tiene una API completamente diferente:

Stripe usa makeCharge(amount, currency)
PayPal usa sendMoney(account, total)
La aplicación necesita un método unificado processPayment(amount)

Problema 2 — Variación independiente:
Los tipos de pago y los proveedores deben evolucionar de forma independiente:

Agregar un nuevo proveedor NO debe afectar los tipos de pago
Agregar un nuevo tipo de pago NO debe afectar los proveedores

Solución
Patrón 1 — Adapter
Traduce la API de cada proveedor externo a una interfaz unificada.
Patrón 2 — Bridge
Separa los tipos de pago de los proveedores para que ambos puedan evolucionar de forma independiente.
