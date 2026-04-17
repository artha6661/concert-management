INSERT INTO concerts (
  name, venue, starts_at, booking_opens_at, booking_closes_at, total_tickets, remaining_tickets
)
VALUES
  (
    'EDTS Live at Central Hall',
    'Central Hall',
    (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') + INTERVAL '3 days',
    (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') + INTERVAL '2 hours',
    10000,
    10000
  ),
  (
    'Tiny Venue Showcase',
    'Studio A',
    (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') + INTERVAL '1 day',
    (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') + INTERVAL '2 hours',
    10,
    10
  );