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

export default function Deductions() {
  const navigate = useNavigate();

  const handleDeductions = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    navigate("/filing/review");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label="Personal Information" status="complete" />
          <StepIndicatorStep label="W-2 Information" status="complete" />
          <StepIndicatorStep label="Self Employment" status="complete" />
          <StepIndicatorStep label="Deductions" status="current" />
          <StepIndicatorStep label="Review" />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handleDeductions} className="w-full">
          <Fieldset legend="This information can be found on form 1099-NEC. If you don't have any Self Employment Income to enter, you can skip this section.">
            {/* Payer and Recipient TIN */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label id="label-payer-tin" htmlFor="payer_tin">
                  Payer's TIN
                </Label>
                <TextInputMask
                  id="payer_tin"
                  name="payer_tin"
                  type="text"
                  aria-labelledby="label-payer-tin"
                  mask="__-_______"
                  pattern="^\d{2}-\d{7}"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label id="label-recipient-tin" htmlFor="recipient_tin">
                  Recipient's TIN
                </Label>
                <TextInputMask
                  id="recipient_tin"
                  name="recipient_tin"
                  type="text"
                  aria-labelledby="label-recipient-tin"
                  mask="___-__-____"
                  pattern="\d{3}-\d{2}-\d{4}"
                  required
                />
              </Grid>
            </Grid>

            {/* NEC */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="label-nec">Non Employee Compensation</Label>
                <span id="hint-nec" className="usa-hint">
                  Box 1 on 1099-NEC
                </span>
                <TextInput
                  id="nec"
                  name="nec"
                  type="number"
                  aria-describedby="hint-nec"
                  aria-labelledby="label-nec"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label id="label-withheld" htmlFor="withheld">
                  Federal Income Tax Withheld
                </Label>
                <span id="hint-witheld" className="usa-hint">
                  Box 4 on 1099-NEC
                </span>
                <TextInput
                  id="withheld"
                  name="withheld"
                  type="number"
                  aria-describedby="hint-withheld"
                  aria-labelledby="label-withheld"
                  required
                />
              </Grid>
            </Grid>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/self-employment"
                  className="usa-button usa-button--outline"
                >
                  Back
                </Link>
                <Link
                  to="/filing/review"
                  className="usa-button usa-button--base"
                >
                  Skip
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
