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

export default function SelfEmployment() {
  const navigate = useNavigate();

  const handleSelfEmploymentInfo = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      payerTIN: e.currentTarget.elements.payer_tin.value,
      // @ts-expect-error untyped form elements but we need the values
      recipientTIN: e.currentTarget.elements.recipient_tin.value,
      // @ts-expect-error untyped form elements but we need the values
      nec: e.currentTarget.elements.nec.value,
      // @ts-expect-error untyped form elements but we need the values
      withheld: e.currentTarget.elements.withheld.value,
    };

    console.log(formData);
    navigate("/filing/deductions");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label="Personal Information" status="complete" />
          <StepIndicatorStep label="W-2 Information" status="complete" />
          <StepIndicatorStep label="Self Employment" status="current" />
          <StepIndicatorStep label="Deductions" />
          <StepIndicatorStep label="Review" />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handleSelfEmploymentInfo} className="w-full">
          <Fieldset legend="This information can be found on form 1099-NEC. If you don't have any Self Employment Income to enter, you can skip this section.">
            {/* Payer and Recipient TIN */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label id="label-payer-tin" htmlFor="payer_tin" requiredMarker>
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
                <Label
                  id="label-recipient-tin"
                  htmlFor="recipient_tin"
                  requiredMarker
                >
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
                <Label htmlFor="label-nec" requiredMarker>
                  Non Employee Compensation
                </Label>
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
                <Label id="label-withheld" htmlFor="withheld" requiredMarker>
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
                  to="/filing/w2"
                  className="usa-button usa-button--outline"
                >
                  Back
                </Link>
                <Link
                  to="/filing/deductions"
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
