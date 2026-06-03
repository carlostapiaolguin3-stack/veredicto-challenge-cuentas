# Desafío técnico · API de Cuentas

Microservicio **Spring Boot con arquitectura hexagonal**. Ya viene **funcionando**: implementa la
consulta de una cuenta por su número. Tu trabajo es **agregar funcionalidad nueva siguiendo el mismo
patrón**.

> No partes de cero: tienes un ejemplo real y completo como referencia. Queremos ver **cómo
> extiendes** un código existente, no si memorizas sintaxis.

---

## 1. Qué ya está hecho (tu referencia)

- `GET /api/v1/cuentas/{numero}` — consulta una cuenta. Funciona end-to-end.
- Estructura hexagonal: `domain/` (núcleo sin frameworks), `application/ports/{input,output}` +
  `application/service` (casos de uso), `infrastructure/{rest,persistence}` (adapters).
- Validación del número en el value object `NumeroCuenta` (6–20 dígitos).
- Manejo de errores centralizado en `GlobalExceptionHandler`.
- Datos en memoria en `CuentaEnMemoria` (cuentas `123456`, `999999`, `555555`, `777000`).
- Tests de ejemplo en `src/test`.

## 2. Cómo correrlo

```bash
./mvnw spring-boot:run
# en otra terminal:
curl http://localhost:8080/api/v1/cuentas/123456
# tests:
./mvnw test
```

## 3. Tu tarea

**La evaluación que recibiste te indica QUÉ funcionalidad(es) implementar** de esta lista. No hagas
de más ni de menos de lo que se te pidió — parte de lo que medimos es que entregues exactamente lo
solicitado.

### F1 · Consultar saldo disponible
`GET /api/v1/cuentas/{numero}/saldo` → devuelve el **saldo disponible**
(`saldoTotal + lineaCredito`; ya existe el método `Cuenta.saldoDisponible()`).
- 200 con `{ "numero": "...", "saldoDisponible": ... }`
- 404 si la cuenta no existe · 400 si el número es inválido.

### F2 · Listar cuentas por estado
`GET /api/v1/cuentas?estado=ACTIVA` → devuelve la **lista** de cuentas en ese estado.
- Requiere **extender el puerto de salida** (`CuentaRepository`) con un método nuevo,
  implementarlo en `CuentaEnMemoria`, crear el **caso de uso** y el **endpoint**.
- 200 con la lista (puede ir vacía) · 400 si el estado no es válido.

### F3 · Bloquear una cuenta
`POST /api/v1/cuentas/{numero}/bloquear` → cambia el estado a `BLOQUEADA`.
- **Regla de negocio:** no se puede bloquear una cuenta `CERRADA` → responde 409.
- Requiere un método para **persistir** el cambio en el puerto de salida + manejo del nuevo error.

## 4. Qué evaluamos

- Que **respetes la arquitectura** (dominio sin frameworks, puertos, adapters, casos de uso).
- **Validación** y **manejo de errores** consistentes con lo existente.
- **Tests** de lo que agregas (al menos uno por funcionalidad pedida).
- **Git**: trabaja en una rama, commits con mensajes claros (no "wip wip fix").
- Que entregues **lo pedido** — ni incompleto ni inflado.

## 5. Cómo entregar

1. Haz **fork** de este repositorio a tu cuenta de GitHub.
2. Trabaja en una rama (no `main`), commitea a medida que avanzas.
3. Abre un **Pull Request** contra el `main` de tu fork.
4. En la descripción del PR cuenta brevemente: qué hiciste, qué decisiones tomaste, qué dejaste fuera.
5. **Pega el enlace del Pull Request en tu evaluación.**

No esperamos perfección. Queremos ver cómo trabajas. Suerte.
