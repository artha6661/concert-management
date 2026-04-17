CREATE TABLE concerts (
  id              BIGSERIAL PRIMARY KEY,
  name            VARCHAR(200) NOT NULL,
  venue           VARCHAR(200) NOT NULL,
  starts_at       TIMESTAMPTZ  NOT NULL,
  booking_opens_at TIMESTAMPTZ NOT NULL,
  booking_closes_at TIMESTAMPTZ NOT NULL,
  total_tickets   INTEGER      NOT NULL,
  remaining_tickets INTEGER    NOT NULL,
  version         BIGINT       NOT NULL DEFAULT 0,
  created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
  updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
  CONSTRAINT concerts_tickets_nonnegative CHECK (total_tickets >= 0 AND remaining_tickets >= 0),
  CONSTRAINT concerts_remaining_le_total CHECK (remaining_tickets <= total_tickets),
  CONSTRAINT concerts_booking_window_valid CHECK (booking_opens_at < booking_closes_at)
);

CREATE INDEX idx_concerts_starts_at ON concerts(starts_at);
CREATE INDEX idx_concerts_booking_window ON concerts(booking_opens_at, booking_closes_at);
CREATE INDEX idx_concerts_remaining ON concerts(remaining_tickets);

CREATE TABLE bookings (
  id           BIGSERIAL PRIMARY KEY,
  concert_id   BIGINT      NOT NULL REFERENCES concerts(id),
  user_id      VARCHAR(100) NOT NULL,
  quantity     INTEGER     NOT NULL,
  status       VARCHAR(30) NOT NULL,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT bookings_quantity_positive CHECK (quantity > 0)
);

CREATE INDEX idx_bookings_concert_id ON bookings(concert_id);
CREATE INDEX idx_bookings_user_id ON bookings(user_id);

