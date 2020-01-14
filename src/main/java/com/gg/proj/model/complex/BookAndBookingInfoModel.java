package com.gg.proj.model.complex;

import com.gg.proj.model.BookEntity;

import java.time.LocalDate;
import java.util.Objects;

public class BookAndBookingInfoModel {

    private BookEntity bookEntity;
    private LocalDate nextReturnDate;
    private Long bookingQueue;
    private boolean bookReturned;
    private boolean queueToLong;

    public BookAndBookingInfoModel() {
    }

    public BookAndBookingInfoModel(BookEntity bookEntity, LocalDate nextReturnDate, Long bookingQueue, boolean bookReturned, boolean queueToLong) {
        this.bookEntity = bookEntity;
        this.nextReturnDate = nextReturnDate;
        this.bookingQueue = bookingQueue;
        this.bookReturned = bookReturned;
        this.queueToLong = queueToLong;
    }

    public BookEntity getBookEntity() {
        return bookEntity;
    }

    public void setBookEntity(BookEntity bookEntity) {
        this.bookEntity = bookEntity;
    }

    public LocalDate getNextReturnDate() {
        return nextReturnDate;
    }

    public void setNextReturnDate(LocalDate nextReturnDate) {
        this.nextReturnDate = nextReturnDate;
    }

    public Long getBookingQueue() {
        return bookingQueue;
    }

    public void setBookingQueue(Long bookingQueue) {
        this.bookingQueue = bookingQueue;
    }

    public boolean isBookReturned() {
        return bookReturned;
    }

    public void setBookReturned(boolean bookReturned) {
        this.bookReturned = bookReturned;
    }

    public boolean isQueueToLong() {
        return queueToLong;
    }

    public void setQueueToLong(boolean queueToLong) {
        this.queueToLong = queueToLong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookAndBookingInfoModel that = (BookAndBookingInfoModel) o;
        return queueToLong == that.queueToLong &&
                bookEntity.equals(that.bookEntity) &&
                Objects.equals(nextReturnDate, that.nextReturnDate) &&
                Objects.equals(bookingQueue, that.bookingQueue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookEntity, nextReturnDate, bookingQueue, queueToLong);
    }

    @Override
    public String toString() {
        return "BookAndBookingInfoModel{" +
                "bookEntity=" + bookEntity +
                ", nextReturnDate=" + nextReturnDate +
                ", bookingQueue=" + bookingQueue +
                ", bookReturned=" + bookReturned +
                ", queueToLong=" + queueToLong +
                '}';
    }
}
