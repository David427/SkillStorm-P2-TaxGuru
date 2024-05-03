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
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label={t("personal.title")} status="complete" />
          <StepIndicatorStep label={t("filing-info.title")} status="complete" />
          <StepIndicatorStep label="W-2 Information" status="complete" />
          <StepIndicatorStep label="Self Employment" status="complete" />
          <StepIndicatorStep label="Credits & Deductions" status="complete" />
          <StepIndicatorStep label="Review" status="current" />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handleReviewInfo} className="w-full">
          <Fieldset legend="Review the information and ensure it is correct. If you see any errors, click the section header to edit the values.">
            {/* Personal */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/personal">Personal Information</Link>
              </h4>
              {/* Fname Lname Birthdate */}
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

              {/* SSN & Filing Status */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label id="label-ssn" htmlFor="ssn">
                    SSN or TIN
                  </Label>
                  <TextInputMask
                    id="ssn"
                    name="ssn"
                    type="text"
                    aria-labelledby="label-ssn"
                    mask="___-__-____"
                    pattern="\d{3}-\d{2}-\d{4}"
                    defaultValue="123-45-6789"
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-filing_status" htmlFor="filing_status">
                    Filing Status
                  </Label>
                  <TextInput
                    id="filing_status"
                    name="filing_status"
                    type="text"
                    aria-labelledby="label-filing_status"
                    defaultValue="Single"
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            {/* W2 */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/w2">W-2 Information</Link>
              </h4>

              {/* Employer Name, Wages, & Taxes Withheld */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label id="label-employer_name" htmlFor="employer_name">
                    Employer Name
                  </Label>
                  <TextInput
                    id="employer_name"
                    name="employer_name"
                    type="text"
                    aria-labelledby="label-employer_name"
                    defaultValue={"Skillstorm Commercial Services, LLC"}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-wages" htmlFor="wages">
                    Wages
                  </Label>
                  <TextInput
                    id="wages"
                    name="wages"
                    type="number"
                    aria-labelledby="label-wages"
                    defaultValue={100_000}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-w2_withheld" htmlFor="w2_withheld">
                    Federal Taxes Withheld
                  </Label>
                  <TextInput
                    id="w2_withheld"
                    name="w2_withheld"
                    type="number"
                    aria-labelledby="label-w2_withheld"
                    defaultValue={10_000}
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

            {/* Self Employment */}
            <section>
              <h4 className="margin-bottom-0">
                <Link to="/filing/self-employment">
                  Self Employment Information
                </Link>
              </h4>

              {/* Non-Employment Compensation & Taxes Withheld */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label id="label-nec_wages" htmlFor="nec_wages">
                    Non-Employee Compensation
                  </Label>
                  <TextInput
                    id="nec_wages"
                    name="nec_wages"
                    type="number"
                    aria-labelledby="label-nec_wages"
                    defaultValue={10_000}
                    disabled
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label id="label-nec_withheld" htmlFor="nec_withheld">
                    Federal Taxes Withheld
                  </Label>
                  <TextInput
                    id="nec_withheld"
                    name="nec_withheld"
                    type="number"
                    aria-labelledby="label-nec_withheld"
                    defaultValue={1_000}
                    disabled
                  />
                </Grid>
              </Grid>
            </section>

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
