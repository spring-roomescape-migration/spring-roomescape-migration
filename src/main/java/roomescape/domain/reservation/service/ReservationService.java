package roomescape.domain.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.domain.Reservations;
import roomescape.domain.reservation.domain.repository.ReservationRepository;
import roomescape.domain.reservation.error.exception.ReservationErrorCode;
import roomescape.domain.reservation.error.exception.ReservationException;
import roomescape.domain.reservation.service.dto.AdminReservationRequest;
import roomescape.domain.reservation.service.dto.ReservationRequest;
import roomescape.domain.reservation.service.dto.ReservationResponse;
import roomescape.domain.reservation.service.dto.ReservationWaitRequest;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.service.ThemeService;
import roomescape.domain.time.domain.Time;
import roomescape.domain.time.service.TimeService;
import roomescape.domain.waiting.domain.WaitingRanks;
import roomescape.domain.waiting.service.WaitingService;
import roomescape.domain.waiting.service.dto.WaitingResponse;

import java.util.ArrayList;
import java.util.List;

import static roomescape.domain.reservation.utils.DateTimeCheckUtil.isBeforeCheck;
import static roomescape.domain.reservation.utils.FormatCheckUtil.reservationDateFormatCheck;
import static roomescape.domain.reservation.utils.FormatCheckUtil.reservationNameFormatCheck;

@Service
public class ReservationService {

    private final TimeService timeService;
    private final ThemeService themeService;
    private final MemberService memberService;
    private final WaitingService waitingService;
    private final ReservationRepository reservationRepository;

    public ReservationService(TimeService timeService, ThemeService themeService, MemberService memberService, WaitingService waitingService, ReservationRepository reservationRepository) {
        this.timeService = timeService;
        this.themeService = themeService;
        this.memberService = memberService;
        this.waitingService = waitingService;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, Member loginMember) {
        Time time = timeService.findById(reservationRequest.getTimeId());
        Theme theme = themeService.findById(reservationRequest.getThemeId());
        validationCheck(reservationRequest.getName(), reservationRequest.getDate(), time);
        Reservation reservation = new Reservation(null, reservationRequest.getName(), reservationRequest.getDate(), reservationRequest.getStatus(), theme, time, null);
        loginMember.addReservation(reservation);
        Long id = reservationRepository.save(reservation);
        Reservation savedReservation = findById(id);
        return mapToReservationResponseDto(savedReservation);
    }

    @Transactional
    public WaitingResponse waitForReservation(ReservationWaitRequest reservationRequest, Member loginMember) {
        Time time = timeService.findById(reservationRequest.getTimeId());
        Theme theme = themeService.findById(reservationRequest.getThemeId());
        validationCheck(loginMember.getName(), reservationRequest.getDate(), time);
        return waitingService.save(time, theme, loginMember, reservationRequest.getDate());
    }

    @Transactional
    public ReservationResponse adminSave(AdminReservationRequest adminReservationRequest) {
        Time time = timeService.findById(adminReservationRequest.getTimeId());
        Theme theme = themeService.findById(adminReservationRequest.getThemeId());
        Member member = memberService.findById(adminReservationRequest.getMemberId());
        Reservation reservation = new Reservation(null, member.getName(), adminReservationRequest.getDate(), adminReservationRequest.getStatus(), theme, time, member);
        member.addReservation(reservation);
        Long id = reservationRepository.save(reservation);
        Reservation savedReservation = findById(id);
        return mapToReservationResponseDto(savedReservation);
    }

    @Transactional(readOnly = true)
    public Reservation findById(long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ReservationException(ReservationErrorCode.INVALID_RESERVATION_DETAILS_ERROR));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(this::mapToReservationResponseDto).toList();
    }

    public List<ReservationResponse> findAllByMemberId(Long id) {
        Reservations reservations = new Reservations(reservationRepository.findAllByMemberId(id));
        WaitingRanks waitingRanks = new WaitingRanks(waitingService.findWaitingRankByMemberId(id));
        return combineResponses(reservations, waitingRanks);
    }

    @Transactional
    public void delete(Long id) {
        Reservation reservation = findById(id);
        reservationRepository.delete(reservation);
    }

    private void validationCheck(String name, String date, Time time) {
        reservationNameFormatCheck(name);
        reservationDateFormatCheck(date);
        isBeforeCheck(date, time.getStartAt());
    }

    private List<ReservationResponse> combineResponses(Reservations reservations, WaitingRanks waitingRanks) {
        List<ReservationResponse> combinedResponses = new ArrayList<>();
        combinedResponses.addAll(reservations.mapToReservationResponseDto());
        combinedResponses.addAll(waitingRanks.mapToReservationResponseDto());
        return combinedResponses;
    }

    private ReservationResponse mapToReservationResponseDto(Reservation reservation) {
        return new ReservationResponse(reservation);
    }
}
