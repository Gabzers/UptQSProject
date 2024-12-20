/**
 * @file Manages the coordinator's evaluation editing functionality.
 * @version 1.0
 * @since 2023-10-10
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */

document.addEventListener('DOMContentLoaded', function () {
    const startDateInput = document.getElementById('evaluation-date-start');
    const endDateInput = document.getElementById('evaluation-date-end');
    const examPeriod = document.getElementById('evaluation-exam-period');

    if (!examPeriod.value) {
        startDateInput.disabled = true;
        endDateInput.disabled = true;
    }

    examPeriod.addEventListener('change', function () {
        if (examPeriod.value) {
            startDateInput.disabled = false;
            endDateInput.disabled = false;
            highlightValidDates();
        } else {
            startDateInput.disabled = true;
            endDateInput.disabled = true;
        }
    });

    endDateInput.addEventListener('change', function () {
        assignRoomAutomatically();
    });
});

/**
 * Validates the form inputs.
 * @returns {boolean} True if the form is valid, otherwise false.
 */
function validateForm() {
    const examPeriod = document.getElementById('evaluation-exam-period').value;
    const startDate = document.getElementById('evaluation-date-start').value;
    const endDate = document.getElementById('evaluation-date-end').value;

    if (!examPeriod) {
        document.getElementById('exam-period-error').style.display = 'inline';
        return false;
    }

    if (!startDate || !endDate) {
        alert('Please select both start and end date and time.');
        return false;
    }

    return true;
}

/**
 * Highlights valid dates for the evaluation period.
 */
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

            startDateInput.min = start;
            startDateInput.max = end;
            endDateInput.min = start;
            endDateInput.max = end;
        } else {
            console.error('Failed to fetch valid date ranges');
        }
    } catch (error) {
        console.error('Error fetching valid date ranges:', error);
    }
}

/**
 * Assigns a room automatically based on the selected dates and requirements.
 */
async function assignRoomAutomatically() {
    const startDateInput = document.getElementById('evaluation-date-start').value;
    const endDateInput = document.getElementById('evaluation-date-end').value;
    const computerRequired = document.getElementById('evaluation-computer-required').checked;
    const numStudents = document.getElementById('curricular-unit-students').value;

    if (startDateInput && endDateInput) {
        try {
            const response = await fetch(`/coordinator/availableRooms?startTime=${startDateInput}&endTime=${endDateInput}&computerRequired=${computerRequired}&numStudents=${numStudents}`);
            if (response.ok) {
                const rooms = await response.json();
            } else {
                console.error('Failed to fetch available rooms');
            }
        } catch (error) {
            console.error('Error fetching available rooms:', error);
        }
    }
}
