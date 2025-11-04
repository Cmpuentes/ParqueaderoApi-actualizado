/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author Carlos Mario
 */
public class ConteoActivoDTO {
    
    private long totalActivos;
    
    // 👇 Jackson lo necesita
    public ConteoActivoDTO() {
    }

    public ConteoActivoDTO(long totalActivos) {
        this.totalActivos = totalActivos;
    }

    public long getTotalActivos() {
        return totalActivos;
    }

    public void setTotalActivos(long totalActivos) {
        this.totalActivos = totalActivos;
    }
    
}
