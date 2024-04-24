import {
  Form,
  Grid,
  Label,
  Button,
  Fieldset,
  TextInput,
  ButtonGroup,
  GridContainer,
  StepIndicator,
  TextInputMask,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";

import { Link, useNavigate } from "react-router-dom";

export default function Review() {
  const navigate = useNavigate();

  const handleReviewInfo = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    navigate("/filing/results");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h4" ofText="of" stepText="Step">
          <StepIndicatorStep label="Personal Information" status="complete" />
          <StepIndicatorStep label="W-2 Information" status="complete" />
          <StepIndicatorStep label="Self Employment" status="complete" />
          <StepIndicatorStep label="Deductions" status="complete" />
          <StepIndicatorStep label="Review" status="current" />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handleReviewInfo} className="w-full">
          <Fieldset legend="Review the information and ensure it is correct. If you see any errors, click the section header to edit the values.">
            {/* Personal */}
            <section>
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label id="label-first_name" htmlFor="first_name">
                    First Name
                  </Label>
                  <TextInput
                    id="first_name"
                    name="first_name"
                    aria-labelledby="label-first_name"
                    type="text"
                    defaultValue="First Name"
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-last_name" htmlFor="last_name">
                    Last Name
                  </Label>
                  <TextInput
                    id="last_name"
                    name="last_name"
                    aria-labelledby="label-last_name"
                    type="text"
                    defaultValue="Last Name"
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-birthdate" htmlFor="birthdate">
                    Birthdate
                  </Label>
                  <TextInputMask
                    id="birthdate"
                    name="birthdate"
                    type="text"
                    aria-labelledby="label-birthdate"
                    aria-describedby="hint-birthdate"
                    mask="__/__/____"
                    // the pattern fits the date format MM/DD/YYYY
                    pattern="^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\d{4}$"
                    defaultValue="11/28/2001"
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            {/* W2 */}

            {/* Self Employment */}

            {/* Deductions */}

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/self-employment"
                  className="usa-button usa-button--outline"
                >
                  Back
                </Link>
                <Button type="submit">Continue</Button>
              </ButtonGroup>
            </div>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
