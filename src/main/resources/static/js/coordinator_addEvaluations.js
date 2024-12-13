document.addEventListener('DOMContentLoaded', function () {
    const startDateInput = document.getElementById('evaluation-date-start');
    const endDateInput = document.getElementById('evaluation-date-end');
    const examPeriod = document.getElementById('evaluation-exam-period').value;

    if (!examPeriod) {
        startDateInput.disabled = true;
        endDateInput.disabled = true;
    }

    flatpickr(startDateInput, {
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        time_24hr: true
    });
    flatpickr(endDateInput, {
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        time_24hr: true
    });
});

function validateForm() {
    const examPeriod = document.getElementById('evaluation-exam-period').value;
    if (!examPeriod) {
        document.getElementById('exam-period-error').style.display = 'inline';
        return false;
    }
    return true;
}

async function highlightValidDates() {
    const examPeriod = document.getElementById('evaluation-exam-period').value;
    const startDateInput = document.getElementById('evaluation-date-start');
    const endDateInput = document.getElementById('evaluation-date-end');
    const curricularUnitId = document.getElementById('curricular-unit-id').value;

    // Clear existing date & time values
    startDateInput.value = '';
    endDateInput.value = '';

    if (!examPeriod) {
        startDateInput.disabled = true;
        endDateInput.disabled = true;
        return;
    }

    startDateInput.disabled = false;
    endDateInput.disabled = false;

    try {
        const response = await fetch(`/coordinator/getValidDateRanges?examPeriod=${examPeriod}&curricularUnitId=${curricularUnitId}`);
        if (response.ok) {
            const validDateRanges = await response.json();
            const { start, end } = validDateRanges;

            flatpickr(startDateInput, {
                enableTime: true,
                dateFormat: "Y-m-d\\TH:i", // Adjusted format
                minDate: start,
                maxDate: end,
                theme: "dark",
                onReady: function(selectedDates, dateStr, instance) {
                    highlightPickerDates(instance, start, end);
                },
                onChange: function(selectedDates, dateStr, instance) {
                    highlightPickerDates(instance, start, end);
                }
            });

            flatpickr(endDateInput, {
                enableTime: true,
                dateFormat: "Y-m-d\\TH:i", // Adjusted format
                minDate: start,
                maxDate: end,
                theme: "dark",
                onReady: function(selectedDates, dateStr, instance) {
                    highlightPickerDates(instance, start, end);
                },
                onChange: function(selectedDates, dateStr, instance) {
                    highlightPickerDates(instance, start, end);
                }
            });
        } else {
            console.error('Failed to fetch valid date ranges');
        }
    } catch (error) {
        console.error('Error fetching valid date ranges:', error);
    }
}

function highlightPickerDates(instance, start, end) {
    const validStartDate = new Date(start);
    const validEndDate = new Date(end);

    instance.calendarContainer.querySelectorAll('.flatpickr-day').forEach(dayElem => {
        const date = instance.parseDate(dayElem.dateObj, "Y-m-d");
        if (date >= validStartDate && date <= validEndDate) {
            dayElem.classList.add('valid-date');
        } else {
            dayElem.classList.remove('valid-date');
        }
    });
}
