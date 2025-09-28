package com.example.overlook_hotel.model.entity;

import com.example.overlook_hotel.model.enums.ReservationStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "reservations",
       indexes = @Index(name = "idx_reservations_room_dates", columnList = "room_id,check_in_date,check_out_date"))
public class Reservation {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "lead_guest_name") 
    private String leadGuestName;

    @Column(name = "lead_guest_phone") 
    private String leadGuestPhone;

    @Column(name = "check_in_date", nullable = false) 
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false) 
    private LocalDate checkOutDate;

    @Column(name = "guests_count") 
    private Integer guestsCount = 1;

    @Column(name = "total_price", precision = 10, scale = 2) 
        private BigDecimal totalPrice = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name="status", length=32, nullable=false)
    private ReservationStatus status;

    @Column(name = "special_requests") 
    private String specialRequests;
    
    @Column(name = "cancellation_policy") 
    private String cancellationPolicy;

    @Column(name = "created_at") 
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at") 
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @Version
    @Column(name = "version")
    private Integer version = 1;

    // getters/setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null && (this.leadGuestName == null || this.leadGuestName.isBlank())) {
            // populate leadGuestName if not explicitly provided
            String first = user.getFirstName() == null ? "" : user.getFirstName();
            String last = user.getLastName() == null ? "" : user.getLastName();
            String fullname = (first + " " + last).trim();
            if (!fullname.isEmpty()) {
                this.leadGuestName = fullname;
            }
        }
        if (user != null && (this.leadGuestPhone == null || this.leadGuestPhone.isBlank())) {
            this.leadGuestPhone = user.getPhoneNumber();
        }
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getLeadGuestName() {
        return leadGuestName;
    }

    public void setLeadGuestName(String leadGuestName) {
        this.leadGuestName = leadGuestName;
    }  

    public String getLeadGuestPhone() {
        return leadGuestPhone;
    }

    public void setLeadGuestPhone(String leadGuestPhone) {
        this.leadGuestPhone = leadGuestPhone;
    }
    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getGuestsCount() {
        return guestsCount;
    }

    public void setGuestsCount(Integer guestsCount) {
        this.guestsCount = (guestsCount == null || guestsCount < 1) ? 1 : guestsCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = (totalPrice == null) ? BigDecimal.ZERO : totalPrice;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    // No public setter for createdAt to avoid accidental modification
    protected void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    protected void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
