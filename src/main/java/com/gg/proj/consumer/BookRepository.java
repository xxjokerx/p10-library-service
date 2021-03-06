package com.gg.proj.consumer;

import com.gg.proj.model.BookEntity;
import com.gg.proj.model.complex.BookAndBookingInfoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface, it extends JpaRepository to benefit spring Data / JPA
 */
public interface BookRepository extends JpaRepository<BookEntity, Integer>, BookRepositoryCustom {

    @Query("select distinct b from BookEntity b " +
            "where upper(b.title) like upper(:x) or upper(b.author) like upper(:x) or upper(b.summary) like upper(:x)")
    Page<BookEntity> searchPagedBooks(@Param("x") String keyWord, Pageable pageable);

    @Query("select distinct b from BookEntity b " +
            "where upper(b.title) like upper(:x) or upper(b.author) like upper(:x) or upper(b.summary) like upper(:x)")
    List<BookEntity> searchAllBooks(@Param("x") String keyWord);

    @Modifying
    @Query("UPDATE BookEntity b SET b.quantity = (b.quantity - 1) WHERE b.id = (:id)")
    void decreaseQuantity(@Param("id") int bookId);

    @Modifying
    @Query("UPDATE BookEntity b SET b.quantity = (b.quantity + 1) WHERE b.id = (:id)")
    void increaseQuantity(@Param("id") int bookId);

    @Query("SELECT b.quantity FROM BookEntity b WHERE b.id = (:id)")
    Integer getBookQuantityById(@Param("id") int bookId);

    @Query("SELECT SUM(book.quantity + book.loans.size) " +
            "FROM BookEntity book " +
            "WHERE book.id = (:bookId)")
    Long queryTotalAmountOfBook(@Param("bookId") Integer bookId);

//                      !bookReturned                                                       bookReturned
//            "(((MAX(booking.notificationTime) IS NULL) AND (book.quantity = 0)) OR ((MAX(booking.notificationTime) IS NOT NULL) AND (loan.closed = TRUE))) ," +

    @Query("SELECT new com.gg.proj.model.complex.BookAndBookingInfoModel(" +
            "book, MIN(loan.loanEndDate), " +
            "COUNT(booking), " +
            "(MAX(booking.notificationTime) IS NOT NULL), " +
            "(2*(book.quantity + book.loans.size) <= COUNT(booking)) " +
            ") " +
            "FROM BookEntity book " +
            "LEFT JOIN BookingEntity booking ON book.id = booking.book.id " +
            "LEFT JOIN LoanEntity loan ON book.id = loan.book.id " +
            "GROUP BY book " +
            "HAVING book.id = (:bookId) ")
    BookAndBookingInfoModel customQueryBookAndBookingInfoByBookId(@Param("bookId") Integer bookId);
}
