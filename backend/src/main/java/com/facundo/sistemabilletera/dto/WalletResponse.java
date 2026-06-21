package com.facundo.sistemabilletera.dto;

import java.math.BigDecimal;

public record WalletResponse(
    Long id,
    BigDecimal balance,
    Long userId,
    String userEmail
) {

}
